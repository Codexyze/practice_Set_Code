package com.example.coroutinespratice

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(){
    runBlocking {
        launch(Dispatchers.IO) {
            println("launch 1 started")
            delay(5000)
            println("launch 1 completed Thread : ${Thread.currentThread().name}")
        }
        launch(Dispatchers.IO) {
            println("launch 2 started")
            delay(5000)
            println("launch 2 completed Thread : ${Thread.currentThread().name}")
        }
    }

}
//Output :
//launch 1 started
//launch 2 started
//launch 2 completed Thread : DefaultDispatcher-worker-3
//launch 1 completed Thread : DefaultDispatcher-worker-1

