package com.example.coroutinespratice

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

fun JobExample(){
 GlobalScope.launch(Dispatchers.IO) {
     val time = measureTimeMillis {
         NetworkCall1()
         NetworkCall2()
     }
     Log.d("COROUTINE","Network call ended in $time")
     //Takes 4 sec thats not good it shopuld take 2 sec
 }

}

suspend fun NetworkCall1(): String {
    Log.d("COROUTINE","Network call 1 running")
    delay(2000)
    return "Result1"
    Log.d("COROUTINE","Network call 1 ended")

}
suspend fun NetworkCall2(): String {
    Log.d("COROUTINE","Network call 2 running")
    delay(2000)
    return "Result2"
    Log.d("COROUTINE","Network call 2 ended")
}

fun JobExample2(){
    GlobalScope.launch(Dispatchers.IO) {
        val time = measureTimeMillis {
            val job1 =launch(Dispatchers.IO) {
                val data =NetworkCall1()
                Log.d("COROUTINE" , data.toString())
            }
            val job2 =launch(Dispatchers.IO) {
                val data =NetworkCall2()
                Log.d("COROUTINE" , data.toString())
            }
            job1.join()
            job2.join()

        }
        Log.d("COROUTINE","Network call ended in $time")
    }
   //Network call ended in 2018 ms
}

//Using ASync Await Instead


fun AsyncExample(){
    GlobalScope.launch(Dispatchers.IO) {
        val time = measureTimeMillis {
            val data1 = async(Dispatchers.IO) {
                NetworkCall1()
                //Async Returns Deffered data
                //Deffered<String> = Deffered<T>
            }
            val data2= async(Dispatchers.IO) {
                NetworkCall2()
            }
           val dataAsyn1 = data1.await()
            val data2Async = data2.await() // await waits till we get output
        }
        Log.d("COROUTINE", "Time taken : $time")




    }
}