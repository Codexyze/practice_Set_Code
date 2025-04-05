package com.example.coroutinespratice

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main():Unit = runBlocking {
    var data: String
    coroutineScope {

        launch(Dispatchers.IO) {
            val net= doNetworkCall()
           println(net)
        }
        launch(Dispatchers.Default) {
           //display here
        }
    }
}

suspend fun doNetworkCall(): String{
    delay(3000)
    return "I made a network call"
}