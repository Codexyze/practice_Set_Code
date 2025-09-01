package com.nutrino.paggingwithhilt.Data.Pagging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.nutrino.paggingwithhilt.Data.Local.UserDatabase
import com.nutrino.paggingwithhilt.Data.Mappers.toDomian
import com.nutrino.paggingwithhilt.Data.Mappers.toEntity
import com.nutrino.paggingwithhilt.Data.Remote.User
import com.nutrino.paggingwithhilt.Domain.UseCases.GetAllUserUseCase
import javax.inject.Inject

class UserPaggingSouce @Inject constructor(private val apiUserUseCase: GetAllUserUseCase,
    private val database: UserDatabase
): PagingSource<Int, User>() {
    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
       val page = params.key ?: 1
        val apiResult = apiUserUseCase.invoke(page = page)
        val userEntity = apiResult.map {
            it.toEntity()
        }
        try {
            if (apiResult.isNotEmpty()){
                database.UserDao().insertAllUsers(userEntity = userEntity)
            }

           return LoadResult.Page(
                data =apiResult ,
                prevKey = if (page==1)  null else page-1,
               nextKey = if(apiResult.isNullOrEmpty()) null else page + 1
            )

        }catch (e: Exception){
            val cachedEntities = database.UserDao().getAllUsers()
            val cached = cachedEntities.map { it.toDomian() }

            if (cached.isEmpty()) {
                return LoadResult.Error(e)
            } else if(apiResult.isEmpty() || (apiResult.isNullOrEmpty() && !cached.isNullOrEmpty()) ) {
                return LoadResult.Page(
                    data = cached,
                    prevKey = null,
                    nextKey = null
                )
            }else{
                return LoadResult.Page(
                    data = cached,
                    prevKey = null,
                    nextKey = null
                )

            }


        }

    }
}