package com.example.coroutinespratice

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope


fun main(): Unit = runBlocking {
    coroutineScope {
        launch {
            println("This is a coroutine 1 st print")
        }
        launch {

            throw Exception("Sample")
        }//Exit here in coroutine scope

        launch {
            println("This is a coroutine 2 nd print")
        }
    }//no exception handeling

    supervisorScope {
        launch {
            println("This is a coroutine 1 st print")

        }
        launch {

            throw Exception("Sample")

        }//runs even after that

        launch {
            println("This is a coroutine 2 nd print")
        }

    }

}