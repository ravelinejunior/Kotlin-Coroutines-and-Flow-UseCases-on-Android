package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase10

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lukaslechner.coroutineusecasesonandroid.utils.MainCoroutineScopeRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*

@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
class CalculationInBackgroundViewModelTest {

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

    @Test
    fun `performCalculation shouldPerformCorrectCalculations assertEquals`() =
        mainDispatcherRule.runBlockingTest {
            // Arrange
            val viewModel =
                CalculationInBackgroundViewModel(mainDispatcherRule.testDispatcherRule).apply {
                    observe(this)
                }

            Assert.assertTrue(receivedStates.isEmpty())

            // Act
            viewModel.performCalculation(4)

            // Assert
            Assert.assertEquals(
                UiState.Loading,
                receivedStates.first()
            )

            Assert.assertEquals(
                "24",
                (receivedStates[1] as UiState.Success).result
            )

        }

    private fun observe(viewModel: CalculationInBackgroundViewModel) {
        viewModel.uiState().observeForever {
            if (it != null) {
                receivedStates.add(it)
            }
        }
    }
}