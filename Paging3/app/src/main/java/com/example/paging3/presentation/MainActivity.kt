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
import androidx.compose.ui.platform.LocalContext
import androidx.room.Database

// -------------------- MainActivity --------------------

// @AndroidEntryPoint is a Hilt annotation that tells Dagger-Hilt to provide dependencies to this Activity.
// Hilt generates a Component that lives as long as this Activity. It helps with Dependency Injection (DI).
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // onCreate is the entry point of an Activity. It is called when Activity is first created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // enableEdgeToEdge() is a helper to make UI content draw behind system bars (status/nav)
        // Important for modern full-screen UI designs.
        enableEdgeToEdge()

        // setContent replaces setContentView in Compose world.
        // Here we define the Composable UI directly instead of XML layouts.
        setContent {
            // Paging3Theme is a custom Compose Material3 theme (likely defined elsewhere)
            // Wrap your app UI in a theme to provide typography, colors, and shapes.
            Paging3Theme {

                // LocalContext provides the Android context in a Composable scope
                // Useful for Room DB or starting new Activities
                val context = LocalContext.current

                // Scaffold is a Material3 layout composable for standard UI structure:
                // TopBar, BottomBar, FAB, content padding handling.
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // Box allows stacking elements on top of each other
                    // Here we use innerPadding to account for scaffold content padding
                    Box(modifier = Modifier.padding(innerPadding)) {

                        // Create API service instance to fetch users
                        val apiService = UserApiService()

                        // Create ViewModel manually passing context and API service
                        // This ViewModel handles paging and Room caching logic
                        val viewModel = UserViewModel(context, apiService)

                        // Composable that renders a LazyColumn with paged user items
                        UserListScreen(viewModel)
                    }
                }
            }
        }
    }
}

// -------------------- Ktor Client --------------------

// KtorClient singleton object creates a reusable HttpClient
// CIO = Coroutine-based I/O engine
// ContentNegotiation + json allows automatic serialization/deserialization with Kotlinx.serialization
object KtorClient {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true }) // ignore unknown JSON fields
        }
    }
}

// API service class to fetch users from a REST API
// page parameter supports paging in APIs that accept it
class UserApiService {
    suspend fun fetchUsers(page: Int): List<User> {
        return KtorClient.client.get("https://jsonplaceholder.typicode.com/users") {
            parameter("page", page)
        }.body()
    }
}

// -------------------- Room DB --------------------

// Room Entity represents a table in the SQLite database
// Each property maps to a column in the "users" table
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int, // primary key uniquely identifies a row
    val name: String,
    val username: String,
    val email: String
)

// Convert domain User model to DB entity
fun User.toEntity() = UserEntity(id, name, username, email)

// Convert DB entity back to domain User model
fun UserEntity.toDomain() = User(
    id = id,
    name = name,
    username = username,
    email = email,
    address = Address("", "", "", "", Geo("", "")), // minimal placeholder
    phone = "",
    website = "",
    company = Company("", "", "")
)

// DAO (Data Access Object) defines database operations for UserEntity
@Dao
interface UserDao {

    // Insert or replace a list of users
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<UserEntity>)

    // Fetch all users from the DB
    @Query("SELECT * FROM users")
    suspend fun getAll(): List<UserEntity>
}

// RoomDatabase abstract class defines the database
@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao // Room generates implementation
}

// Helper function to create Room DB instance
fun provideDatabase(context: android.content.Context) =
    Room.databaseBuilder(context, AppDatabase::class.java, "app_db").build()

// -------------------- Paging Source --------------------

// PagingSource fetches pages of data from API or DB
// Key = Int (page number), Value = User (item type)
class UserPagingSource(
    private val apiService: UserApiService,
    private val userDao: UserDao
) : PagingSource<Int, User>() {

    // Refresh key decides which page to load on refresh
    override fun getRefreshKey(state: PagingState<Int, User>): Int? = 1

    // load is called to load a page of data
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        val page = params.key ?: 1 // default to page 1 if null
        return try {
            val users = apiService.fetchUsers(page) // fetch users from API

            // Cache fetched users in Room for offline support
            if (users.isNotEmpty()) {
                userDao.insertAll(users.map { it.toEntity() })
            }

            // Return a successful page to Paging library
            LoadResult.Page(
                data = users,
                prevKey = if (page == 1) null else page - 1, // null if first page
                nextKey = if (users.isEmpty()) null else page + 1 // null if no more data
            )
        } catch (e: Exception) {
            // If API fails, fallback to cached data from Room
            val cached = userDao.getAll().map { it.toDomain() }
            if (cached.isNotEmpty()) {
                LoadResult.Page(data = cached, prevKey = null, nextKey = null)
            } else {
                LoadResult.Error(e) // propagate error if no cache
            }
        }
    }
}

// -------------------- ViewModel --------------------

// ViewModel holds UI data and paging flow
// context is passed for Room DB, apiService for network calls
class UserViewModel(context: android.content.Context, apiService: UserApiService) : ViewModel() {
    private val db = provideDatabase(context) // create Room DB
    private val userDao = db.userDao() // get DAO

    // PagingData flow emits pages of users to Compose
    val userFlow: Flow<PagingData<User>> = Pager(
        config = PagingConfig(pageSize = 10), // load 10 items per page
        pagingSourceFactory = { UserPagingSource(apiService, userDao) }
    ).flow.cachedIn(viewModelScope) // cache in ViewModel scope to survive rotations
}

// -------------------- Domain Model --------------------

// Serializable annotation allows Kotlinx.serialization to parse JSON
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

// Main Composable displaying paginated user list
@Composable
fun UserListScreen(viewModel: UserViewModel) {
    // collectAsLazyPagingItems converts PagingData flow to items for LazyColumn
    val userPagingItems = viewModel.userFlow.collectAsLazyPagingItems()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        // itemCount is dynamic, depends on loaded pages
        items(userPagingItems.itemCount) { index ->
            val user = userPagingItems[index] // get item at index
            user?.let { UserItem(user) } // display each user
        }
    }
}

// Individual user card
@Composable
fun UserItem(user: User, isDark: Boolean = isSystemInDarkTheme()) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), // margin around card
        elevation = CardDefaults.cardElevation(4.dp) // shadow elevation
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Spacer(modifier = Modifier.width(16.dp)) // spacing before content
            Column {
                Text(text = user.name, style = MaterialTheme.typography.titleMedium)
                Text(text = user.email, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

/*
==================== THEORY & MEMORY TIPS ====================

1️⃣ Paging3 Theory (offline + online):
- PagingSource loads pages from network or local DB.
- Pager + Flow emits PagingData which Compose collects lazily.
- cachedIn(viewModelScope) keeps paging alive during config changes.
- Offline support: Cache API data in Room -> fallback to Room on failure.

2️⃣ Room Offline Paging:
- Insert API response in DB.
- PagingSource tries API first, fallback to DB if network fails.
- Keeps app responsive and usable offline.

3️⃣ Tips / Memory points:
- "LazyColumn + collectAsLazyPagingItems" = lazy loading UI
- "prevKey / nextKey" controls paging navigation
- "Insert onConflict = REPLACE" avoids duplicates
- "cachedIn(viewModelScope)" = survives rotations
- Always provide context for Room if not using DI

4️⃣ Ktor Client:
- Single instance reduces memory overhead.
- ContentNegotiation + Json handles automatic (de)serialization.

5️⃣ Domain vs Entity:
- Entity = DB table
- Domain = business logic / API model
- Convert between them using extension functions.

6️⃣ Compose + Paging:
- Compose observes Flow<PagingData<T>> reactively.
- LazyColumn renders only visible items, efficient memory.

=================================================================
*/
