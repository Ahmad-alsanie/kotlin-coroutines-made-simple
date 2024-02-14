package com.sanie

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CoroutineSpringBootApp

fun main(args: Array<String>) {
    runApplication<CoroutineSpringBootApp>(*args)
}