package com.example.paging3.PraticeSet

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.paging3.presentation.User
import com.example.paging3.presentation.UserApiService

class UserPagingSource2(private  val userApiService: UserApiService): PagingSource<Int, User>(){
    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
       return 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        var currentPage= if(params.key == null){
             1
        }else{
            params.key!!.toInt()
        }
        val data = userApiService.fetchUsers(page = currentPage)
         return try {
             LoadResult.Page(
                 data = data,
                 prevKey = if(currentPage == 1){
                     null
                 }else{
                     currentPage-1
                 },
                 nextKey = if(data.isEmpty()){
                     null
                 }else{
                     currentPage+1
                 }
             )
         } catch (e: Exception){
             return LoadResult.Error(e)
         }
    }

}