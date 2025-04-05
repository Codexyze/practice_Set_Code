package com.example.coroutinespratice

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main():Unit = runBlocking {
    repeat(50_000) { // launch a lot of coroutines
        launch(Dispatchers.IO) {
            delay(5000L)
            print("number $it ${Thread.currentThread().name}")

        }
    }
}