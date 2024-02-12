package com.sanie.channel

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

fun CoroutineScope.produceNumbers(): ReceiveChannel<Int> = produce {
    var x = 1
    while (true) {
        send(x++)
        delay(100L) // Wait for 100ms before sending the next number
    }
}

fun CoroutineScope.launchProcessor(id: Int, channel: ReceiveChannel<Int>) = launch {
    for (msg in channel) {
        println("Processor #$id received $msg")
    }
}

fun main() = runBlocking {
    val producer = produceNumbers() // Start producing numbers
    repeat(5) { id ->
        launchProcessor(id, producer) // Launch 5 processors
    }
    delay(950L) // Let the processors receive numbers for a bit
    producer.cancel() // Cancel the producer to stop producing numbers

    // Example of a buffered channel
    val bufferedChannel = Channel<Int>(100) // Create a channel with a buffer of 100
    launch {
        for (x in 1..5) {
            bufferedChannel.send(x) // Send numbers to the buffered channel
            println("Sent $x")
        }
        bufferedChannel.close() // Close the channel after sending the numbers
    }

    // Receive numbers from the buffered channel
    for (y in bufferedChannel) {
        println("Received $y")
    }
}
