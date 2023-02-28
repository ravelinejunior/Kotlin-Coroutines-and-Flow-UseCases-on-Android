package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase14

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.utils.MainCoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.*
import org.junit.Assert.fail

class AndroidVersionRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainCoroutineScopeRule()

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `loadAndStoreRemoteAndroidVersions shouldContinueToLoadAndStoreAndroidVersionsWhenCallingScopeGetsCancelled assertEquals`() {

        //Arrange
        val fakeDatabase = FakeDatabase()
        val fakeApi = FakeSuccessMockApi()

        val repository = AndroidVersionRepository(fakeDatabase, mainDispatcherRule, fakeApi)
        val testViewModelScope = TestCoroutineScope(SupervisorJob())

        //Act
        testViewModelScope.launch {
            repository.loadAndStoreRemoteAndroidVersions()
            fail("Scope should be cancelled before versions are loaded!")
        }

        testViewModelScope.cancel()
        testViewModelScope.testScheduler.apply {
            advanceUntilIdle()
        }

        //Assert
        Assert.assertEquals(
            true,
            fakeDatabase.insertedIntoDb
        )

    }
}












