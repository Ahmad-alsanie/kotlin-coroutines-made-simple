package com.sanie.lunch
import kotlinx.coroutines.*

fun main() {
    runBlocking { // CoroutineScope
        launch { // launch a new coroutine in the background and continue
            delay(1000L) // non-blocking delay for 1 second
            println("Simple! By A.Sanie") // print after the delay
        }
        println("Made,") // main coroutine continues here immediately
    }
}
