package com.example.paging3.data.KtorClient

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.paging3.data.remote.CharacterResponse
import com.example.paging3.data.remote.Result
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
//
//class CharacterApiService {
//    suspend fun fetchCharacters(url: String): CharacterResponse {
//        return KtorClient.ktorClent.get(url).body()
//    }
//}
//
//
//class CharacterPagingSource(private val apiService: CharacterApiService) : PagingSource<Int, Character>() {
//    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
//       return state.anchorPosition?.let { position->
//           val page = state.closestPageToPosition(position)
//           page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
//
//       }
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
//        TODO("Not yet implemented")
//    }
//
//}
//
//class CharacterViewModel(private val apiService: CharacterApiService) : ViewModel() {
//    val characterFlow: Flow<PagingData<Character>> = Pager(
//        config = PagingConfig(
//            pageSize = 20,
//            enablePlaceholders = false
//        ),
//        pagingSourceFactory = { CharacterPagingSource(apiService) }
//    ).flow.cachedIn(viewModelScope)
//}
//
//
//@Composable
//fun CharacterList(viewModel: CharacterViewModel) {
//    val characterPagingItems = viewModel.characterFlow.collectAsLazyPagingItems()
//
//    LazyColumn {
//        items(characterPagingItems.itemSnapshotList.items) { character ->
//            character?.let {
//                Text(text = it)
//                AsyncImage(model = it.image, contentDescription = it.name)
//            }
//        }
//    }
//}
