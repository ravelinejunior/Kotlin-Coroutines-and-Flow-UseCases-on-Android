package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase2

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import com.lukaslechner.coroutineusecasesonandroid.mock.mockVersionFeaturesAndroid10
import com.lukaslechner.coroutineusecasesonandroid.utils.ReplaceMainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class Perform2SequentialNetworkRequestsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val replaceMainDispatcherRule: ReplaceMainDispatcherRule = ReplaceMainDispatcherRule()

    private val receivedUiStates = mutableListOf<UiState>()

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
    }

    @Test
    fun `perform2SequentialNetworkRequest shouldReturnSuccessWhenBothNetworkRequestAreSuccessful assertEquals`() {

        // Arrange
        val mockApi = FakeSuccessMockApi()
        val viewModel = arrangeInit(mockApi)

        // Act
        viewModel.perform2SequentialNetworkRequest()

        // Assertion
        assertEquals(
            listOf(
                UiState.Loading,
                UiState.Success(mockVersionFeaturesAndroid10)
            ), receivedUiStates
        )
    }

    @Test
    fun `perform2SequentialNetworkRequest shouldReturnFailureWhenOneNetworkRequestItsFailure assertEquals`() {

        // Arrange
        val mockApi = FakeErrorMockApi()
        val viewModel = arrangeInit(mockApi)

        // Act
        viewModel.perform2SequentialNetworkRequest()

        // Assertion
        assertEquals(
            listOf(
                UiState.Loading,
                UiState.Error("Api Level Not Found!")
            ),
            receivedUiStates
        )
    }

    private fun arrangeInit(mockApi: MockApi): Perform2SequentialNetworkRequestsViewModel {

        val viewModel = Perform2SequentialNetworkRequestsViewModel(mockApi)

        initializeObservers(viewModel)
        return viewModel
    }

    private fun initializeObservers(viewModel: Perform2SequentialNetworkRequestsViewModel) {
        viewModel.uiState().observeForever { uiState ->
            if (uiState != null) {
                receivedUiStates.add(uiState)
            }
        }
    }
}











