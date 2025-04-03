package com.example.paging3.PraticeSet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.paging3.presentation.User
import com.example.paging3.presentation.UserApiService
import com.example.paging3.presentation.UserItem
import kotlinx.coroutines.flow.Flow

class UserPagingSource2(private  val userApiService: UserApiService): PagingSource<Int, User>(){
    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {

        return try {
            val currentPage = params.key ?: 1  // Default to page 1 if no key provided
            val users = userApiService.fetchUsers(page = currentPage)

            LoadResult.Page(
                data = users,
                prevKey = if (currentPage == 1) null else currentPage - 1,  // No previous page if on page 1
                nextKey = if (users.isEmpty()) null else currentPage + 1    // If API returns empty, no more pages
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}


    class MyViewModel(private val apiService: UserApiService) : ViewModel() {
        fun getDataThroughPaging(): Flow<PagingData<User>> = Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { UserPagingSource2(apiService) }
        ).flow.cachedIn(viewModelScope)
    }



@Composable
fun ListOfUsers() {
    val apiService = UserApiService()
    val viewmodel = MyViewModel(apiService)
    val userListPagr = viewmodel.getDataThroughPaging().collectAsLazyPagingItems()

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(userListPagr.itemCount) {index->
                val useritem = userListPagr[index]
                useritem?.let {
                    UserItem(user = it)
                }
            }
        }
    }
}


