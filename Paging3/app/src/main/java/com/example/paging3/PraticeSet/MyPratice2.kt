package com.example.paging3.PraticeSet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import com.example.paging3.presentation.User
import com.example.paging3.presentation.UserApiService
import com.example.paging3.presentation.UserItem
import com.example.paging3.presentation.UserPagingSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

import androidx.paging.compose.collectAsLazyPagingItems


class MyViewModel2(private val apiService: UserApiService) : ViewModel() {
    // Use StateFlow to maintain state across recompositions
    private val _uiState = MutableStateFlow<State<Flow<PagingData<User>>>>(State.Loading)
    val uiState: StateFlow<State<Flow<PagingData<User>>>> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        val pager = Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false,
                prefetchDistance = 3
            ),
            pagingSourceFactory = { PaggingSource(apiService) }
        ).flow.cachedIn(viewModelScope)

        _uiState.value = State.Success(pager)
    }

    fun retry() {
        _uiState.value = State.Loading
        loadData()
    }
}

sealed class State<out T> {
    object Loading : State<Nothing>()
    data class Success<T>(val data: T) : State<T>()
    data class Error(val message: String) : State<Nothing>()
}

class PaggingSource(private val apiService: UserApiService) : PagingSource<Int, User>() {
        override fun getRefreshKey(state: PagingState<Int, User>): Int? = state.anchorPosition?.let { anchorPosition ->
//        val anchorPage = state.closestPageToPosition(anchorPosition)
//        anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
            return 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val currentPageNumber = params.key ?: 1
            val dataOfUser = apiService.fetchUsers(page = currentPageNumber)
            LoadResult.Page(
                data = dataOfUser,
                prevKey = if (currentPageNumber == 1) null else currentPageNumber - 1,
                nextKey = if (dataOfUser.isEmpty()) null else currentPageNumber + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

@Composable
fun SimpleUserScreen(viewModel: MyViewModel2) {
    val uiState = viewModel.uiState.collectAsState()

    when (val state = uiState.value) {
        is State.Loading -> SimpleLoadingView()
        is State.Success -> SimpleUserList(pagingDataFlow = state.data)
        is State.Error -> SimpleErrorView(
            message = state.message,
            onRetry = { viewModel.retry() }
        )
    }
}

@Composable
fun SimpleUserList(pagingDataFlow: Flow<PagingData<User>>) {
    val lazyPagingItems = pagingDataFlow.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        items(
            count = lazyPagingItems.itemCount
        ) { index ->
            val user = lazyPagingItems[index]
            user?.let { SimpleUserItem(it) }
        }

        when (lazyPagingItems.loadState.refresh) {
            is LoadState.Error -> item { SimpleRetryItem { lazyPagingItems.retry() } }
            is LoadState.Loading -> item { SimpleLoadingItem() }
            else -> {}
        }
    }
}

@Composable
fun SimpleLoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun SimpleErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = message, color = Color.Red)
        TextButton(onClick = onRetry) { Text("Try Again") }
    }
}

@Composable
fun SimpleUserItem(user: User) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp).background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = CircleShape
                ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.name.firstOrNull()?.uppercase() ?: "U",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Column(
                modifier = Modifier.padding(start = 12.dp).weight(1f)
            ) {
                Text(text = user.name, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun SimpleLoadingItem() {
    Box(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
    }
}

@Composable
private fun SimpleRetryItem(onRetry: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Failed to load")
        TextButton(onClick = onRetry, modifier = Modifier.padding(start = 8.dp)) {
            Text("Retry")
        }
    }
}