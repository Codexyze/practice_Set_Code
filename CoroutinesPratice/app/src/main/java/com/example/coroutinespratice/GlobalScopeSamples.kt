package com.example.coroutinespratice

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun GlobalScope(){
    GlobalScope.launch(Dispatchers.IO) {
        launch(Dispatchers.IO) {
            println("launch 1 started")
            Log.d("COROUTINE", "launch 1 started T1")
            delay(5000)
            Log.d("COROUTINE", "launch 1 completed Thread : ${Thread.currentThread().name}")
        }
        launch(Dispatchers.IO) {

            Log.d("COROUTINE", "launch 2 started T1")
            delay(5000)
            Log.d("COROUTINE", "launch 2 completed Thread : ${Thread.currentThread().name}")
        }
    }
    GlobalScope.launch(Dispatchers.IO) {
        launch(Dispatchers.IO) {
            println("launch 1 started")
            Log.d("COROUTINE", "launch 1 started T2")
            delay(5000)
            Log.d("COROUTINE", "launch 1 completed Thread : ${Thread.currentThread().name}")
        }
        launch(Dispatchers.IO) {

            Log.d("COROUTINE", "launch 2 started T2")
            delay(5000)
            Log.d("COROUTINE", "launch 2 completed Thread : ${Thread.currentThread().name}")
        }
    }
}

fun GlobalScopeExample2(){
    val job = GlobalScope.launch {
        repeat(5) {
            Log.d("COROUTINE", "launch 1 started $it")
            delay(2000)
        }
    }
    GlobalScope.launch(Dispatchers.IO) {
        job.join()
        Log.d("COROUTINE", "launch 1 completed Thread : ${Thread.currentThread().name}")
    }
}