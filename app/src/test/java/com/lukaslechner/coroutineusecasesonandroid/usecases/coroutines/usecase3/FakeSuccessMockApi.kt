package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import com.lukaslechner.coroutineusecasesonandroid.mock.*
import com.lukaslechner.coroutineusecasesonandroid.utils.EndpointShouldNotBeCalledException
import kotlinx.coroutines.delay

class FakeSuccessMockApi(
    private val responseDelay:Long = 0L
) : MockApi {
    override suspend fun getRecentAndroidVersions(): List<AndroidVersion> {
        throw EndpointShouldNotBeCalledException()
    }

    override suspend fun getAndroidVersionFeatures(apiLevel: Int): VersionFeatures {
        delay(responseDelay)
        return when (apiLevel) {
            27 -> mockVersionFeaturesOreo
            28 -> mockVersionFeaturesPie
            29 -> mockVersionFeaturesAndroid10
            else -> throw IllegalArgumentException("Api Level Not Found!")
        }
    }
}