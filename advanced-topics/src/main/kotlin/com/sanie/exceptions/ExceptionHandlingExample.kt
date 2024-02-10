package com.sanie.exceptions

import kotlinx.coroutines.*

fun main() = runBlocking {
    // try-catch with launch
    val jobWithTryCatch = launch {
        try {
            println("Launching a coroutine that will fail...")
            throw IllegalArgumentException("Something went wrong in the launched coroutine!")
        } catch (e: Exception) {
            println("Caught an exception in try-catch block: $e")
        }
    }
    jobWithTryCatch.join()

    // Global CoroutineExceptionHandler
    val globalExceptionHandler = CoroutineExceptionHandler { _, exception ->
        println("Handled by CoroutineExceptionHandler: $exception")
    }

    // CoroutineExceptionHandler with launch
    val jobWithExceptionHandler = launch(globalExceptionHandler) {
        println("Launching a coroutine that will fail and be handled by CoroutineExceptionHandler...")
        throw IndexOutOfBoundsException("Something went wrong in the coroutine with exception handler!")
    }
    jobWithExceptionHandler.join()

    // exception handling in async-await
    val deferredWithError = async {
        println("Async operation started, which will fail...")
        throw ArithmeticException("Problem in async operation")
    }
    try {
        deferredWithError.await()
    } catch (e: Exception) {
        println("Caught an exception from async-await: $e")
    }

    println("Done.")
}
