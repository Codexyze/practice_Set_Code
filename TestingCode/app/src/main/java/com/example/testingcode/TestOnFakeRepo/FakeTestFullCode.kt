package com.example.testingcode.TestOnFakeRepo

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ---------- MODEL ----------
data class User(
    val id: Int,
    val name: String
)

// ---------- REPOSITORY ----------
interface UserRepository {
    suspend fun getUsers(): List<User>
}

class FakeUserRepository : UserRepository {
    override suspend fun getUsers(): List<User> {
        return listOf(
            User(1, "Akshay"),
            User(2, "Ramesh"),
            User(3, "Neha")
        )
    }
}


// ---------- VIEWMODEL ----------

class UserViewModel(
    private val repository: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO // inject for test
) {
    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    fun fetchUsers() {
        // Launch with provided dispatcher (test will inject TestDispatcher)
        CoroutineScope(dispatcher).launch {
            val result = repository.getUsers()
            _users.value = result
        }
    }
}


