package com.example.coroutinespratice.Couroutines

import kotlinx.coroutines.*


suspend fun fetchHeadlines(): String {
    delay(2000) // pretend API call
    return "📰 Top Headlines Loaded"
}

suspend fun fetchWeather(): String {
    delay(1500) // pretend API call
    return "🌤️ Weather Loaded"
}

suspend fun fetchStocks(): String {
    delay(1000) // pretend API call
    return "📈 Stock Market Loaded"
}

suspend fun loadHomeScreenData() = coroutineScope {
    // Run all three tasks in parallel
    val headlinesDeferred = async { fetchHeadlines() }
    val weatherDeferred = async { fetchWeather() }
    val stocksDeferred = async { fetchStocks() }

    // Wait for all results
    val headlines = headlinesDeferred.await()
    val weather = weatherDeferred.await()
    val stocks = stocksDeferred.await()

    println("✅ Home Screen Data Loaded:")
    println(headlines)
    println(weather)
    println(stocks)
}

fun main() = runBlocking {
    val startTime = System.currentTimeMillis()

    loadHomeScreenData()

    val endTime = System.currentTimeMillis()
    println("⏱️ Total Time: ${endTime - startTime} ms")
}
