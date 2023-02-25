package com.lukaslechner.coroutineusecasesonandroid.utils

class EndpointShouldNotBeCalledException(
    private val errorMessage: String? = null
) : Throwable(errorMessage) {
}