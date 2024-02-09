package com.sanie.async

import kotlinx.coroutines.*

fun main() = runBlocking {
    val startTime = System.currentTimeMillis()
    val deferredResult = async {
        delay(1000L) // simulate a long-running task
        "Result of computation"
    }

    println("Computation started, main thread is free to do other things.")
    println("Waiting for the result...")
    val result = deferredResult.await() // waits for the result without blocking the main thread
    println("Result: $result")
    println("Completed in ${System.currentTimeMillis() - startTime} ms")
}
