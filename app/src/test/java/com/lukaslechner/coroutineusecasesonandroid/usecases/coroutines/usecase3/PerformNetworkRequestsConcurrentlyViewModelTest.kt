package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase3

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesAndroid10
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesOreo
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesPie
import com.lukaslechner.coroutineusecasesonandroid.utils.MainCoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*

@ExperimentalCoroutinesApi
class PerformNetworkRequestsConcurrentlyViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainCoroutineScopeRule()

    private val receivedStates = mutableListOf<UiState>()

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `performNetworkRequestsSequentially shouldLoadDataSequentially assertEquals`() =
        mainDispatcherRule.runBlockingTest {

            // Arrange
            val responseDelayTime = 1000L
            val mockApi = FakeSuccessMockApi(responseDelayTime)
            val viewModel = PerformNetworkRequestsConcurrentlyViewModel(mockApi)
            observeViewModel(viewModel)

            // Act
            viewModel.performNetworkRequestsSequentially()
            val forwardedTime = testScheduler.apply { advanceUntilIdle() }

            // Assert
            Assert.assertEquals(
                listOf(
                    UiState.Loading, UiState.Success(
                        listOf(
                            mockVersionFeaturesOreo,
                            mockVersionFeaturesPie,
                            mockVersionFeaturesAndroid10
                        )
                    )
                ), receivedStates
            )

            Assert.assertEquals(
                3000, forwardedTime.currentTime
            )
        }

    @ExperimentalCoroutinesApi
    @Test
    fun `performNetworkRequestsConcurrently shouldLoadDataConcurrently assertEquals`() =
        mainDispatcherRule.runBlockingTest {

            // Arrange
            val responseDelay = 1000L
            val mockApi = FakeSuccessMockApi(responseDelay)
            val viewModel = PerformNetworkRequestsConcurrentlyViewModel(mockApi)
            observeViewModel(viewModel)

            // Act
            viewModel.performNetworkRequestsConcurrently()
            val forwardedTime = testScheduler.apply { advanceUntilIdle() }

            // Assert
            Assert.assertEquals(
                listOf(
                    UiState.Loading, UiState.Success(
                        listOf(
                            mockVersionFeaturesOreo,
                            mockVersionFeaturesPie,
                            mockVersionFeaturesAndroid10
                        )
                    )
                ), receivedStates
            )

            Assert.assertEquals(
                1000, forwardedTime.currentTime
            )
        }

    private fun observeViewModel(viewModel: PerformNetworkRequestsConcurrentlyViewModel) {
        viewModel.uiState().observeForever { uiState ->
            if (uiState != null) {
                receivedStates.add(uiState)
            }
        }
    }
}