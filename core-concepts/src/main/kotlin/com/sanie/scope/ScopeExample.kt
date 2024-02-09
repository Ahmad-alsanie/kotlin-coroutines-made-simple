package com.sanie.scope
import kotlinx.coroutines.*

fun main() {
    GlobalScope.launch { // launch a new coroutine in global scope
        delay(1000L)
        println("Simple!")
    }
    println("Made,")
    Thread.sleep(2000L) // block the main thread for 2 seconds to keep JVM alive
}
