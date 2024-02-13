package com.sanie.select
import kotlinx.coroutines.*
import kotlinx.coroutines.NonCancellable.isActive
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.select

fun CoroutineScope.produceNumbers(side: Int): ReceiveChannel<Int> = produce {
    for (i in 1..10) {
        delay((100..200L).random()) // simulate work
        send(i * side)
    }
}

suspend fun receiveNumbers(channelA: ReceiveChannel<Int>, channelB: ReceiveChannel<Int>) {
    while (isActive) { // Continue until the coroutine is active
        select<Unit> {
            channelA.onReceiveCatching { result ->
                result.getOrNull()?.let { println("Channel A: $it") }
            }
            channelB.onReceiveCatching { result ->
                result.getOrNull()?.let { println("Channel B: $it") }
            }
        }
    }
}

fun main() = runBlocking {
    val channelA = produceNumbers(1)
    val channelB = produceNumbers(-1)
    receiveNumbers(channelA, channelB)
}
