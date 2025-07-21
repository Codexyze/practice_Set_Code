package com.nutrino.mviarchitecture

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.nutrino.mviarchitecture.ui.theme.MVIArchitectureTheme
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch


// ------------------------
// ✅ MODEL - Fake data model
// ------------------------
data class User(val id: Int, val name: String)


// ------------------------
// ✅ INTENT - User Actions
// ------------------------
sealed interface UserIntent {
    object LoadUsers : UserIntent
}



// ------------------------
// ✅ STATE - UI State
// ------------------------
sealed interface UserState {
    object Idle : UserState
    object Loading : UserState
    data class Success(val users: List<User>) : UserState
    data class Error(val message: String) : UserState
}


// ------------------------
// ✅ VIEWMODEL - Handles logic
// ------------------------
class UserViewModel : ViewModel() {

    // Backing flow for state (mutable)
    private val _state = MutableStateFlow<UserState>(UserState.Idle)

    // Public immutable flow
    val state: StateFlow<UserState> = _state.asStateFlow()

    // Event handler
    fun onIntent(intent: UserIntent) {
        when (intent) {
            is UserIntent.LoadUsers -> loadUsers()
        }
    }


    // Simulate network call
    private fun loadUsers() {
        viewModelScope.launch {
            _state.value = UserState.Loading
            try {
                delay(2000) // Fake network delay
                val users = listOf(
                    User(1, "Akshay"),
                    User(2, "Neha"),
                    User(3, "Rahul")
                )
                _state.value = UserState.Success(users)
            } catch (e: Exception) {
                _state.value = UserState.Error("Oops! Something went wrong.")
            }
        }
    }
}

// ------------------------
// ✅ MAIN ACTIVITY - UI Layer (View)
// ------------------------
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<viewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                   // UserScreen(viewModel)
                    //MyApiScreen()
                    val state = viewModel.state.collectAsState()
                    Column(modifier = Modifier.fillMaxSize( ), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(onClick = {
                            viewModel.onIntent(intent = StateInterfaceIntent.buttonClicked)
                        }) {
                            Text("Load")
                        }
                        when{
                            state.value.loading->{
                                CircularProgressIndicator()
                            }
                            !state.value.data.isNullOrEmpty()->{
                                Text(state.value.data.toString(), modifier = Modifier.size(500.dp))
                            }


                        }
                    }
                }
            }
        }
    }
}


// ------------------------
// ✅ COMPOSABLE UI
// ------------------------
@Composable
fun UserScreen(viewModel: UserViewModel) {

    // 👇 Automatically collects state updates
    val state = viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        when (val currentState = state.value) {
            is UserState.Idle -> {
                Button(onClick = {
                    viewModel.onIntent(UserIntent.LoadUsers)
                }) {
                    Text("Load Users")
                }
            }

            is UserState.Loading -> {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("Loading users...")
            }

            is UserState.Success -> {
                Text("Users loaded:")
                Spacer(modifier = Modifier.height(8.dp))
                currentState.users.forEach {
                    Text("• ${it.name}")
                }
            }

            is UserState.Error -> {
                Text("Error: ${currentState.message}")
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    viewModel.onIntent(UserIntent.LoadUsers)
                }) {
                    Text("Retry")
                }
            }
        }

    }
}
data class MemeApi(
    val data:String = ""
)

class repository(){
    suspend fun fakeApiCall(): Flow<ResultState<List<MemeApi>>> = flow {
        emit(ResultState.loading)
        delay(500) // simulate network
        val memeList = mutableListOf<MemeApi>()
        for (i in 1..5) {
            memeList.add(MemeApi("Hello $i"))
            delay(500)
        }
        emit(ResultState.Sucess(memeList))
        // If you want error: emit(ResultState.Error("Failed to load memes"))
    }
}
sealed class ResultState<out T>{
    object loading: ResultState<Nothing>()
    data class Sucess<T>(val data:T): ResultState<T>()
    data class Error(val error: String): ResultState<String>()

}

data class State(
    val failuer: String="",
    val data:List<MemeApi> =emptyList(),
    val loading: Boolean = false
    
)

sealed interface StateInterfaceIntent{
    object buttonClicked: StateInterfaceIntent
}

class viewModel(): ViewModel(){
    val repository = repository()
    val job = SupervisorJob()
    val scope = CoroutineScope(job + Dispatchers.Main)
  val state = MutableStateFlow(State())
    
    fun onIntent(intent : StateInterfaceIntent){
        when(intent){
            StateInterfaceIntent.buttonClicked -> {
                viewmodelfun()
            }
            
        }
    }

    fun viewmodelfun(){
       viewModelScope.launch {
           repository.fakeApiCall().collect {result->
               when(result){
                   is ResultState.loading -> {
                       state.value = State(loading =  true)
                   }
                   is ResultState.Sucess->{
                       state.value = State(data = result.data)
                   }
                   is ResultState.Error->{
                       //no need
                   }
               }

           }
       }
    }
}

@Composable
fun MyApiScreen() {

    val viewmodel  = viewModel()



}