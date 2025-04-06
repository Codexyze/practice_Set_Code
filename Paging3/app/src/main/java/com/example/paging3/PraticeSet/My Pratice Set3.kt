package com.example.paging3.PraticeSet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.flow.Flow

class PaggingSource3(private val apiService: UserApiService): PagingSource<Int , User>(){
    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
      return 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val currentPagenumber = params.key ?:1
       val data = apiService.fetchUsers(page = currentPagenumber)
        try {
            return LoadResult.Page(
                data =  data,
                prevKey = if(currentPagenumber==1)null else currentPagenumber-1,
                nextKey = currentPagenumber+1
            )
        }catch (e: Exception){
            return LoadResult.Error(
                throwable = e
            )
        }
    }

}

class MyPersonalViewMode(private val apiSource: UserApiService): ViewModel(){
    fun getdataInPages(): Flow<PagingData<User>>{
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = {
              PaggingSource3(apiService = apiSource)
            }
        ).flow.cachedIn(viewModelScope)

    }

}

@Composable
fun ScreenPagging(modifier: Modifier = Modifier) {
    val apiService = UserApiService()
    val myViewModel = MyPersonalViewMode(apiSource = apiService)
    val data = myViewModel.getdataInPages().collectAsLazyPagingItems()
    Column(modifier= Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.padding(8.dp)) {
            items(data.itemCount) {index->
                val paggingList = data[index]
                Column {
                  Text("${paggingList?.name}")
                    Text("${paggingList?.website}")
                    Text("${paggingList?.email}")
                }

            }
        }
    }

}