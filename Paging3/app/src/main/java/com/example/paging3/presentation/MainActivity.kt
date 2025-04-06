package com.example.paging3.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.paging3.PraticeSet.MyViewModel2
import com.example.paging3.PraticeSet.ScreenPagging
import com.example.paging3.PraticeSet.SimpleUserScreen
import com.example.paging3.ui.theme.Paging3Theme
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Paging3Theme {
                Scaffold(modifier = Modifier.Companion.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.Companion.padding(innerPadding)) {
                        val apiService = UserApiService()
                        val viewModel = UserViewModel(apiService)
                       // UserList(viewModel)
                        //ListOfUsers()
                        val viewmodel= MyViewModel2(apiService)
                        SimpleUserScreen(viewmodel)
                        //ScreenPagging()
                    }
                }
            }
        }
    }
}

object KtorClient {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
}

class UserApiService {
    suspend fun fetchUsers(page:Int): List<User> {
        return KtorClient.client.get("https://jsonplaceholder.typicode.com/users"){
            parameter("page",page)
        }.body()
    }
}

class UserPagingSource(private val apiService: UserApiService) : PagingSource<Int, User>() {
    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
       return 1
    }
//    override fun getRefreshKey(state: PagingState<Int, User>): Int? = state.anchorPosition?.let { anchorPosition ->
//        val anchorPage = state.closestPageToPosition(anchorPosition)
//        anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val currentPage = params.key ?: 1  // Default to page 1 if no key provided
            val users = apiService.fetchUsers(page = currentPage)

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

class UserViewModel(private val apiService: UserApiService) : ViewModel() {

    val userFlow: Flow<PagingData<User>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { UserPagingSource(apiService) }
    ).flow.cachedIn(viewModelScope)
}

@Serializable
data class User(
    val id: Int,
    val name: String,
    val username: String,
    val email: String,
    val address: Address,
    val phone: String,
    val website: String,
    val company: Company
)

@Serializable
data class Address(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: Geo
)

@Serializable
data class Geo(
    val lat: String,
    val lng: String
)

@Serializable
data class Company(
    val name: String,
    val catchPhrase: String,
    val bs: String
)

@Composable
fun UserList(viewModel: UserViewModel) {
    val userPagingItems = viewModel.userFlow.collectAsLazyPagingItems()

    LazyColumn {
        items(userPagingItems.itemCount) { index ->
            val user = userPagingItems[index]
            user?.let {
                UserItem(user)
            }
        }
    }
}

@Composable
fun UserItem(user: User, isDark: Boolean = isSystemInDarkTheme()) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {

            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = user.name, style = MaterialTheme.typography.titleMedium)
                Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}