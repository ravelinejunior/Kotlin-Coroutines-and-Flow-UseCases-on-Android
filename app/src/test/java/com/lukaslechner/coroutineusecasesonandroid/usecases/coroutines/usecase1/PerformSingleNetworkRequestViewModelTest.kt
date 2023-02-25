package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase1

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.mock.mockAndroidVersions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class PerformSingleNetworkRequestViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private lateinit var dispatcher: TestCoroutineDispatcher

    @Before
    fun setUp() {
        dispatcher = TestCoroutineDispatcher()
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        dispatcher.cleanupTestCoroutines()
    }

    private val receivedUiStates = mutableListOf<UiState>()

    @Test
    fun `performSingleNetworkRequest shouldReturnSuccessWhenRequestIsSuccessfully ReturnSuccess`() {

        //Arrange
        val fakeSuccessApi = FakeSuccessApi()
        val viewModel = PerformSingleNetworkRequestViewModel(fakeSuccessApi)

        observeUiState(viewModel)

        //Act
        viewModel.performSingleNetworkRequest()

        //Assert
        Assert.assertEquals(
            listOf(
                UiState.Loading,
                UiState.Success(mockAndroidVersions)
            ), receivedUiStates
        )

    }

    @Test
    fun `performSingleNetworkRequest networkRequestFails assertEquals`() {

        // Arrange
        val fakeApi = FakeErrorApi()
        val viewModel = PerformSingleNetworkRequestViewModel(fakeApi)
        observeUiState(viewModel)

        // Act
        viewModel.performSingleNetworkRequest()

        // Assert
        Assert.assertEquals(
            listOf(
                UiState.Loading,
                UiState.Error("Network Request failed!")
            ),
            receivedUiStates
        )
    }

    private fun observeUiState(viewModel: PerformSingleNetworkRequestViewModel) {
        viewModel.uiState().observeForever { uiState ->

            if (uiState != null) {
                receivedUiStates.add(uiState)
            }

        }
    }
}