package com.sanie.mutable.state

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

// A shared counter class protected by a mutex
class SafeCounter {
    private var counter = 0
    private val mutex = Mutex()

    // increment the counter safely
    suspend fun increment() {
        mutex.withLock {
            counter++
        }
    }


    suspend fun getValue(): Int = mutex.withLock { counter }
}

fun main() = runBlocking<Unit> {
    val safeCounter = SafeCounter()
    val numberOfCoroutines = 100
    val incrementTimes = 1000

    // launch multiple coroutines to increment the counter
    val jobs = List(numberOfCoroutines) {
        launch(Dispatchers.Default) {
            repeat(incrementTimes) {
                safeCounter.increment()
            }
        }
    }

    // Wait for all to complete
    jobs.forEach { it.join() }

    // read the final value of the counter
    val finalCount = safeCounter.getValue()
    println("Expected final count: ${numberOfCoroutines * incrementTimes}")
    println("Actual final count: $finalCount")

    // check for consistency
    if (finalCount == numberOfCoroutines * incrementTimes) {
        println("The counter was incremented safely and consistently!")
    } else {
        println("There was a problem with incrementing the counter safely.")
    }
}