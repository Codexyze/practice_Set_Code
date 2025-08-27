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

//Room

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.room.Database


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Paging3Theme {
                val context = LocalContext.current

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        val apiService = UserApiService()
                        val viewModel = UserViewModel(context, apiService)
                        UserListScreen(viewModel)
                    }
                }
            }
        }
    }
}

// -------------------- Ktor Client --------------------
object KtorClient {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
}

class UserApiService {
    suspend fun fetchUsers(page: Int): List<User> {
        return KtorClient.client.get("https://jsonplaceholder.typicode.com/users") {
            parameter("page", page)
        }.body()
    }
}

// -------------------- Room DB --------------------
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val username: String,
    val email: String
)

fun User.toEntity() = UserEntity(id, name, username, email)
fun UserEntity.toDomain() = User(
    id = id,
    name = name,
    username = username,
    email = email,
    address = Address("", "", "", "", Geo("", "")),
    phone = "",
    website = "",
    company = Company("", "", "")
)

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    @Query("SELECT * FROM users")
    suspend fun getAll(): List<UserEntity>
}

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

fun provideDatabase(context: android.content.Context) =
    Room.databaseBuilder(context, AppDatabase::class.java, "app_db").build()

// -------------------- Paging Source --------------------
class UserPagingSource(
    private val apiService: UserApiService,
    private val userDao: UserDao
) : PagingSource<Int, User>() {
    override fun getRefreshKey(state: PagingState<Int, User>): Int? = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val page = params.key ?: 1
        return try {
            val users = apiService.fetchUsers(page)
            if (users.isNotEmpty()) {
                userDao.insertAll(users.map { it.toEntity() }) // cache in Room
            }
            LoadResult.Page(
                data = users,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (users.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            // fallback to cached data
            val cached = userDao.getAll().map { it.toDomain() }
            if (cached.isNotEmpty()) {
                LoadResult.Page(data = cached, prevKey = null, nextKey = null)
            } else {
                LoadResult.Error(e)
            }
        }
    }
}

// -------------------- ViewModel --------------------
class UserViewModel(context: android.content.Context, apiService: UserApiService) : ViewModel() {
    private val db = provideDatabase(context)
    private val userDao = db.userDao()

    val userFlow: Flow<PagingData<User>> = Pager(
        config = PagingConfig(pageSize = 10),
        pagingSourceFactory = { UserPagingSource(apiService, userDao) }
    ).flow.cachedIn(viewModelScope)
}

// -------------------- Domain Model --------------------
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

// -------------------- Compose UI --------------------
@Composable
fun UserListScreen(viewModel: UserViewModel) {
    val userPagingItems = viewModel.userFlow.collectAsLazyPagingItems()
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(userPagingItems.itemCount) { index ->
            val user = userPagingItems[index]
            user?.let { UserItem(user) }
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
        Row(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = user.name, style = MaterialTheme.typography.titleMedium)
                Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
