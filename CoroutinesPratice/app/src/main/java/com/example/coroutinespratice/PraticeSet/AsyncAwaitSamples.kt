package com.example.coroutinespratice.PraticeSet

import kotlinx.coroutines.*


//
//fun main() = runBlocking{
//    val fast:Deferred<Int> =  async {
//        var result =0
//        for(i in 1..10){
//            result = result+i
//            println("seconds for fast : $i")
//            delay(1000)
//        }
//        delay(3000)
//        println("Done calculating fast ")
//
//        result
//
//    }
//
//    val slow:Deferred<Int> =  async {
//        var result =0
//        for(i in 1..10){
//            result = result+i
//            println("seconds for slow : $i")
//            delay(1000)
//        }
//        delay(2000)
//        println("Done calculating slow ")
//
//        result
//
//    }
//    val fastVal = fast.await()
//    val slowVal = slow.await()
//
//    println(fastVal+slowVal)
//
//}

suspend fun getID():Int{
    delay(5000)
    return 123
}

suspend fun getAll(id:Int):String{
    if(id==123){
        delay(2000)
        return "my posts are loaded"
    }else{
        return "failed to load"
    }

}

fun main()=runBlocking {
    val id: Deferred<Int> = async {
        getID()
    }

    val getAllPost = getAll(id = id.await())
    println("Waited ...")

    println(getAllPost)


}