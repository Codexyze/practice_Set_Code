package com.example.testingcode

import com.example.testingcode.TestOnFakeRepo.FakeUserRepository
import com.example.testingcode.TestOnFakeRepo.UserViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class UserViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: UserViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = UserViewModel(FakeUserRepository(), testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun fetchUsers_shouldUpdateStateWithDummyUserList() = runTest {
        viewModel.fetchUsers()

        // Manually advance the dispatcher so coroutine executes
        advanceUntilIdle()

        val result = viewModel.users.value
        assertEquals(3, result.size)
        assertEquals("Akshay", result[0].name)
    }
}