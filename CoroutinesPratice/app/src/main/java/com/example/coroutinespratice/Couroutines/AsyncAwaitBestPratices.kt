package com.example.coroutinespratice.Couroutines
// -------------------------------------------------------------------------------------------------
// ✅ DEEP THEORY (READ ME FIRST) — Coroutines, async/await, Deferred, coroutineScope, runBlocking
// -------------------------------------------------------------------------------------------------
//
// 🧠 What is a Coroutine?
// - A coroutine is a lightweight, cooperative unit of work managed by Kotlin’s coroutine runtime.
// - It is NOT a thread. Many coroutines can run on the same thread. They suspend instead of block.
//
// 🧵 Thread vs Coroutine (big difference):
// - Thread.sleep(...) blocks a thread (expensive, prevents others from running on it).
// - delay(...) suspends a coroutine (cheap, frees the thread for other coroutines).
//
// 🧩 Suspension vs Blocking:
// - "Suspending" means the coroutine parks itself and returns control to the dispatcher; later it resumes.
// - "Blocking" means you hold on to the thread and nothing else can use it.
//
// 🌳 Structured Concurrency (why your code is safe here):
// - Parents own children. If the parent scope fails or is cancelled, all children are cancelled.
// - You typically launch work in a scope (e.g., coroutineScope { ... } or viewModelScope.launch { ... }).
// - This prevents leaks and orphaned background tasks. It’s a core Kotlin principle.
//
// 🧰 coroutineScope { ... } (used below):
// - Creates a new suspendable scope that *waits for all child coroutines to finish*.
// - If any child fails, coroutineScope cancels the rest and throws the exception upward.
// - Perfect for starting multiple child tasks (via async/launch) and awaiting them together.
//
// 🚀 async { ... }:
// - Starts a new child coroutine immediately (eager by default).
// - Returns a Deferred<T> — a handle/promise representing a future result.
// - Use async when you want to run *independent* tasks in parallel and later combine their results.
// - If you don’t need a return value (fire-and-forget), use launch instead.
//
// 🧾 Deferred<T>:
// - Think of Deferred as a "future" or "promise" that will eventually hold a value (or an exception).
// - It has await() to retrieve the result, suspending until it’s available.
// - If the child coroutine fails, await() will rethrow that exception in your caller.
//
// ⏳ await():
// - Suspends the *current* coroutine until the Deferred’s result is ready.
// - If the result is ready, it returns immediately; if not, it suspends until completion.
// - Crucially, while you are not awaiting, other work can continue (that’s the parallelism win).
//
// 🧭 When to use async/await?
// - Use when you have multiple *independent* suspend functions that can run at the same time.
//   Example: fetch weather + headlines + stocks from different services.
// - Do NOT use async when calls are *dependent* (B needs A’s result). Do them sequentially.
// - Do NOT spray async everywhere. It complicates error handling without benefit.
//
// 🧯 Exception Handling with async:
// - If a child async fails, the failure will propagate when you call await().
// - In structured concurrency, failing child cancels siblings in the same scope.
// - With coroutineScope, any exception cancels the scope and rethrows, ensuring cleanup.
//
// 🧨 Cancellation:
// - Cancellation is cooperative. Suspending functions check cancellation and abort quickly.
// - If the parent scope is cancelled, children are cancelled. Await throws CancellationException.
//
// 🧭 Dispatchers (not explicitly shown here but important):
// - Dispatchers.Default: CPU-bound work (parsing, hashing).
// - Dispatchers.IO: blocking I/O (network, disk, DB).
// - Dispatchers.Main: UI thread (Android).
// - In pure Kotlin/JVM console apps, runBlocking uses a default event loop/dispatcher.
//
// 🥊 runBlocking { ... } (used for this console demo):
// - Bridges blocking and suspending worlds: it blocks the current thread until the block completes.
// - Great for small console demos & tests. DO NOT use runBlocking on Android main thread.
//
// 🧪 Parallel vs Sequential timing logic (intuition):
// - Sequential total time ≈ sum of durations (A + B + C).
// - Parallel total time ≈ max(duration(A), duration(B), duration(C)).
// - In this demo: 2000ms (headlines), 1500ms (weather), 1000ms (stocks) → parallel ≈ 2000ms.
//
// 🧩 collectLatest vs await (general concept difference for your mental model):
// - await() waits for a *single* Deferred result (one-shot future).
// - Flow.collect{}/collectLatest{} listens to a *stream* of values over time.
// - Different tools for different patterns (one-shot vs streaming).
//
// 🧪 async start modes (FYI):
// - async { ... } is eager (starts immediately).
// - async(start = CoroutineStart.LAZY) starts only when awaited or started manually, useful to define
//   a graph of tasks before actually kicking them off.
//
// 🧠 Design heuristics (quick mental checklist):
// 1) Do I need a return value from multiple independent operations? → async + await.
// 2) Do I just need to "do work" without combining results? → launch.
// 3) Are calls dependent (B needs A)? → sequential call A then B (no async).
// 4) Do I need all children to complete or cancel together? → group them inside coroutineScope.
// 5) Am I on Android? Keep heavy work in IO dispatcher (repo/use-case), collect/update on Main.
//
// 🔚 TL;DR for this file:
// - We simulate 3 parallel API calls using async/await inside coroutineScope.
// - We then await all results, print them, and measure total time.
// - Expect total time to be roughly the longest single delay (≈2000ms).
//
// -------------------------------------------------------------------------------------------------
// END THEORY — Below is the same original code with per-line comments explaining every step.
// -------------------------------------------------------------------------------------------------


import kotlinx.coroutines.* // Imports Coroutine APIs: runBlocking, coroutineScope, async, await, delay, etc.


// Fake suspend function: simulates network call
suspend fun fetchHeadlines(): String {
    delay(2000) // ⏳ Simulate a 2s network delay (suspends coroutine, does NOT block the thread)
    return "📰 Top Headlines Loaded" // ✅ Return a result after the delay
}

suspend fun fetchWeather(): String {
    delay(1500) // ⏳ Simulate a 1.5s network delay
    return "🌤️ Weather Loaded" // ✅ Return a result after the delay
}

suspend fun fetchStocks(): String {
    delay(1000) // ⏳ Simulate a 1s network delay
    return "📈 Stock Market Loaded" // ✅ Return a result after the delay
}

suspend fun loadHomeScreenData() = coroutineScope {
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // coroutineScope:
    // - Creates a structured concurrency scope that will not complete until all its children complete.
    // - If any child throws, coroutineScope cancels siblings and rethrows the exception.
    // - Perfect place to kick off multiple async children that we plan to await.

    // Run all three tasks in parallel
    val headlinesDeferred = async { fetchHeadlines() }
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // async:
    // - Starts a new child coroutine immediately (eager).
    // - Returns Deferred<String> — a handle to the future result.
    // - The body calls fetchHeadlines(), which suspends for 2s, then returns a String.

    val weatherDeferred = async { fetchWeather() }
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // Another async child, independent from headlines; starts right now.
    // This one will complete in ~1.5s.

    val stocksDeferred = async { fetchStocks() }
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // Third async child; starts immediately too.
    // This will complete in ~1s.

    // Wait for all results
    val headlines = headlinesDeferred.await()
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // await():
    // - Suspends this coroutine until headlinesDeferred completes.
    // - If already completed, returns immediately.
    // - If the child failed, await will rethrow its exception here.

    val weather = weatherDeferred.await()
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // Await weather result (≈1.5s from start). By the time we reach here,
    // it's very likely already done because headlines took longer.

    val stocks = stocksDeferred.await()
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // Await stocks result (≈1s from start). Almost certainly completed by now.

    println("✅ Home Screen Data Loaded:")
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // Printing marker that all three results are available.

    println(headlines)
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // Prints the result from fetchHeadlines().

    println(weather)
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // Prints the result from fetchWeather().

    println(stocks)
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // Prints the result from fetchStocks().

    // NOTE:
    // Because we started all three with async in parallel, total time ≈ max(2000, 1500, 1000) = 2000 ms.
    // If we had called them sequentially without async, the total would be ≈ 2000 + 1500 + 1000 = 4500 ms.
}

fun main() = runBlocking {
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // runBlocking:
    // - Entry point for a console app to call suspend functions.
    // - Blocks the current thread until the coroutine inside completes.
    // - Ok for demos/tests; on Android, avoid runBlocking on the main thread.

    val startTime = System.currentTimeMillis()
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // Capture start time to measure elapsed duration.

    loadHomeScreenData()
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // Calls our suspending function. It will:
    // - Create a coroutineScope,
    // - Start three async children in parallel,
    // - Await all three results,
    // - Print them,
    // - Return when done.

    val endTime = System.currentTimeMillis()
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // Capture end time after loadHomeScreenData() completes.

    println("⏱️ Total Time: ${endTime - startTime} ms")
    // ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    // Expectation:
    // - Because the three tasks run in parallel, total time ≈ time of the slowest one (~2000 ms).
    // - You will NOT see ~4500 ms because we avoided sequential execution.
    // - Real numbers may vary slightly due to scheduler overhead, but should be close to 2000 ms.
}
