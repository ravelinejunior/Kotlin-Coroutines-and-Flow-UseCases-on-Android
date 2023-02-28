@file:OptIn(ExperimentalCoroutinesApi::class)

package com.lukaslechner.coroutineusecasesonandroid.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@Suppress("DEPRECATION")
class MainCoroutineScopeRule(
     val testDispatcherRule: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher(), TestCoroutineScope by TestCoroutineScope(testDispatcherRule) {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcherRule)
    }

    override fun finished(description: Description) {
        super.finished(description)
        cleanupTestCoroutines()
        Dispatchers.resetMain()
    }
}