package com.lukaslechner.coroutineusecasesonandroid.usecases.coroutines.usecase13

import androidx.lifecycle.viewModelScope
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.mock.MockApi
import kotlinx.coroutines.*
import timber.log.Timber

class ExceptionHandlingViewModel(
    private val api: MockApi = mockApi()
) : BaseViewModel<UiState>() {

    private val TAG: String = ExceptionHandlingViewModel::class.java.name
    private val ceh = CoroutineExceptionHandler { coroutineContext, throwable ->
        uiState.postValue(UiState.Error("Network error $throwable with ${coroutineContext.job}"))
    }

    fun handleExceptionWithTryCatch() = viewModelScope.launch {

        try {
            uiState.postValue(UiState.Loading)
            api.getAndroidVersionFeatures(27)
        } catch (e: Exception) {
            Timber.tag(TAG).e(e)
            uiState.postValue(UiState.Error("Network error $e with ${coroutineContext.job}"))
        }

    }

    fun handleWithCoroutineExceptionHandler() = viewModelScope.launch(ceh) {
        uiState.postValue(UiState.Loading)

        api.getAndroidVersionFeatures(27)
    }

    fun showResultsEvenIfChildCoroutineFails() = viewModelScope.launch(ceh) {
        uiState.postValue(UiState.Loading)

        supervisorScope {

            val oreoAndroidVersionDeferred = async {
                api.getAndroidVersionFeatures(27)
            }

            val pieAndroidVersionDeferred = async {
                api.getAndroidVersionFeatures(28)
            }

            val android10AndroidVersionDeferred = async {
                api.getAndroidVersionFeatures(29)
            }

            val list = listOf(
                oreoAndroidVersionDeferred,
                pieAndroidVersionDeferred,
                android10AndroidVersionDeferred
            ).mapNotNull {
                try {
                    it.await()
                } catch (e: Exception) {
                    Timber.tag(TAG).e("Error Getting Oreo Features")
                    null
                }
            }

            val oreoFeature = try {
                oreoAndroidVersionDeferred.await()
            } catch (e: Exception) {
                Timber.tag(TAG).e("Error Getting Oreo Features")
                null
            }

            val pieFeature = try {
                pieAndroidVersionDeferred.await()
            } catch (e: Exception) {
                Timber.tag(TAG).e("Error Getting Pie Features")
                null
            }

            val android10Feature = try {
                android10AndroidVersionDeferred.await()
            } catch (e: Exception) {
                Timber.tag(TAG).e("Error Getting Oreo Features")
                null
            }

            val listOfNullOrNothing = listOfNotNull(
                oreoFeature, pieFeature, android10Feature
            )

            if (list.isNotEmpty()) {
                uiState.postValue(UiState.Success(listOfNullOrNothing))
            } else {
                uiState.postValue(UiState.Error("Empty List"))
            }
        }


    }
}