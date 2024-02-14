package com.sanie.controller

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class CoroutineController {

    @GetMapping("/mono")
    fun monoEndpoint(): Mono<String> = Mono.just("Hello, this is a mono endpoint that uses webFlux!")

    @GetMapping("/coroutine")
    suspend fun coroutineEndpoint(): String {
        delay(100) // some processing delay
        return "Hello, this is a coroutine endpoint!"
    }

    @GetMapping("/flow")
    fun flowEndpoint(): Flow<Int> = flow {
        emit(1)
        delay(100) // suspending operation
        emit(2)
    }
}
