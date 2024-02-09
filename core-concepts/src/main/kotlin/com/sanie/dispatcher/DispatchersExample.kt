package com.sanie.dispatcher

import kotlinx.coroutines.*

fun main() = runBlocking {
    launch(Dispatchers.Default) { // launch a coroutine in the Default dispatcher
        println("Default dispatcher. Thread: ${Thread.currentThread().name}")
    }
    launch(Dispatchers.IO) { // launch a coroutine in the IO dispatcher
        println("IO dispatcher. Thread: ${Thread.currentThread().name}")
    }
    launch { // context of the parent, main runBlocking coroutine
        println("Main runBlocking. Thread: ${Thread.currentThread().name}")
    }
}
