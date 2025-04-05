package com.example.coroutinespratice

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main():Unit = runBlocking{
//    launch (Dispatchers.Main){
//        println("${Thread.currentThread().name}")
//
//    }// will not run as main is only present in android
    launch(Dispatchers.IO) {
        println("${Thread.currentThread().name}")
    }
    launch(Dispatchers.Default) {
        println("${Thread.currentThread().name}")
    }
    launch(Dispatchers.Unconfined) {
        println("${Thread.currentThread().name}")
    }
}