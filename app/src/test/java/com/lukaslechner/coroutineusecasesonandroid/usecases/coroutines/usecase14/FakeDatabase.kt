package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase14

import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import kotlinx.coroutines.delay

class FakeDatabase : AndroidVersionDao {

    var insertedIntoDb = false

    override suspend fun getAndroidVersions(): List<AndroidVersionEntity> {
        delay(100)
        return mockAndroidVersions.mapToEntityList()
    }

    override suspend fun insert(androidVersionEntity: AndroidVersionEntity) {
        insertedIntoDb = true
    }

    override suspend fun clear() {}

}
