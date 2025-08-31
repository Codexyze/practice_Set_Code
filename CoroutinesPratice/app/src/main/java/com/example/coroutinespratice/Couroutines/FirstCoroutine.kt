package com.example.coroutinespratice.Couroutines

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val job = launch {
        var i =0
        while(true){
            println("I am working : $i")
            i++
            //delay(100)
            Thread.sleep(100)
        }
    }
    delay(300)
    job.cancel()
}