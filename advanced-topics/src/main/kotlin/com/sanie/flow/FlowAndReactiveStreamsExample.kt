package com.sanie.flow

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.reactive.asPublisher
import kotlinx.coroutines.reactive.collect
import org.reactivestreams.Publisher

// returns a Flow of integers
fun simpleNumberFlow(): Flow<Int> = flow {
    for (i in 1..5) {
        delay(100) // asynchronous work
        emit(i)
    }
}


fun Flow<Int>.square(): Flow<Int> = map { it * it }

fun main() = runBlocking<Unit> {
    // collecting from a Flow
    println("Flow Example:")
    simpleNumberFlow()
        .square() // applying a square
        .collect { value ->
            println(value)
        }

    // Converting Flow to Reactive Streams Publisher
    println("\nReactive Streams Integration:")
    val publisher: Publisher<Int> = simpleNumberFlow().asPublisher(coroutineContext)

    // Subscribing to the Publisher and collecting the items
    publisher.collect { value ->
        println(value)
    }
}
