package com.sanie.main

import kotlinx.coroutines.*
import kotlinx.serialization.*

@Serializable
data class WeatherInfo(val temperature: Double, val description: String)

@Serializable
data class NewsArticle(val title: String, val summary: String)

@Serializable
data class StockInfo(val symbol: String, val price: Double)

suspend fun fetchWeatherInfo(): WeatherInfo {
    // Placeholder for the actual API call
    delay(1000) // Simulating network delay
    return WeatherInfo(24.5, "Sunny")
}

suspend fun fetchNews(): NewsArticle {
    // Placeholder for the actual API call
    delay(1000) // Simulating network delay
    return NewsArticle("Kotlin Coroutines", "Exploring the real-world use cases of coroutines.")
}

suspend fun fetchStockInfo(): StockInfo {
    // Placeholder for the actual API call
    delay(1000) // Simulating network delay
    return StockInfo("KOTLIN", 345.67)
}

fun main() = runBlocking {
    val startTime = System.currentTimeMillis()

    val weatherInfo = async { fetchWeatherInfo() }
    val newsArticle = async { fetchNews() }
    val stockInfo = async { fetchStockInfo() }

    println("Weather: ${weatherInfo.await()}")
    println("News: ${newsArticle.await()}")
    println("Stock: ${stockInfo.await()}")

    val endTime = System.currentTimeMillis()
    println("Fetched all information in ${endTime - startTime} ms")
}
