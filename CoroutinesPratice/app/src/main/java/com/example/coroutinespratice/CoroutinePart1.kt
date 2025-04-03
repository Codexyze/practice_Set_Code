package com.example.coroutinespratice

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking{
    launch {
        first()
    }
    launch {
        second()
    }
}
suspend fun first(){
    var value = 10
    while(true){
        println("first fun Excveution $value++")
        delay(1000)
    }
}

suspend fun second(){
    var value = 0
    while(true){
        println("second  fun Excveution $value++") // this will nevver be executed
        delay(2000)
    }
}

//Output ://first fun Excveution 10++
//second  fun Excveution 0++
//first fun Excveution 10++
//second  fun Excveution 0++
//first fun Excveution 10++
//first fun Excveution 10++
//second  fun Excveution 0++
//first fun Excveution 10++