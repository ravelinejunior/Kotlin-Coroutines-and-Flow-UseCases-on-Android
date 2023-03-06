package com.lukaslechner.coroutineusecasesonandroid.playground.flow.exceptionhandling

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch


private var attempts = 0L

suspend fun main(): Unit = coroutineScope {


    launch {
        stocksFlow()
            .catch { throwable ->
                println("Handle exception in catch() operator $throwable")
            }
            .collect { stockData ->
                println("Collected $stockData")
            }
    }
}

private fun stocksFlow(): Flow<String> = flow {

    repeat(5) { index ->

        delay(500) // Network call

        if (index < 4) {
            emit("New Stock data")
        } else if (attempts < 5) {
            throw NetworkException("Network Request Failed!")
        } else {
            throw Exception("End of loop!")
        }
    }
}.retryWhen { cause, attempt ->
    println("Enter retry() with $cause and attempt is ${attempt + 1}")
    delay(1000 * (attempt + 1))
    attempts = attempt + 1
    cause is NetworkException
}

class NetworkException(message: String) : Exception(message)