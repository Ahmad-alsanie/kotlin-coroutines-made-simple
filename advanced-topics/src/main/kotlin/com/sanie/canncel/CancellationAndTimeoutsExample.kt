package com.sanie.canncel

import kotlinx.coroutines.*

fun main() = runBlocking {
    //cancellation
    val cooperativeJob = launch {
        println("job: Started")
        try {
            repeat(1000) { i ->
                if (!isActive) return@launch // cooperative cancellation check
                println("job: Working... Step $i")
                delay(100L) // simulating work
            }
        } finally {
            println("job: Finally block executed")
        }
    }
    delay(500L) // let the job run for a bit
    println("Main: Cancelling job...")
    cooperativeJob.cancelAndJoin()
    println("Main: job is cancelled.\n")

    // handling a coroutine timeout
    try {
        withTimeout(300L) {
            repeat(100) { i ->
                println("Timeout job: Working... Step $i")
                delay(100L) // simulating work
            }
        }
    } catch (e: TimeoutCancellationException) {
        println("Timeout job: Caught TimeoutCancellationException\n")
    }

    // \using withTimeoutOrNull to handle timeouts more gracefully
    val result = withTimeoutOrNull(300L) {
        repeat(10) { i ->
            println("Graceful timeout job: Working... Step $i")
            delay(100L) // simulating work
        }
        "Graceful timeout job: Completed" // This result will not be returned due to timeout
    }
    println("Result: $result (null means timeout occurred)")
}
