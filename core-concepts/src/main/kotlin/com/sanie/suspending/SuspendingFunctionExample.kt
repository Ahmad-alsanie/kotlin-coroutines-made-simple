package com.sanie.suspending

import kotlinx.coroutines.*

suspend fun computeAnswer(): String {
    delay(1000L) // simulate a long-running operation
    return "Made Simple By A.Sanie"
}

fun main() = runBlocking {
    val result = computeAnswer()
    println("The answer is $result")
}
