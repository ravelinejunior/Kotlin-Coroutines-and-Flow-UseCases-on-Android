package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase3

import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.FlowMockApi
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.Stock
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retryWhen
import retrofit2.HttpException
import timber.log.Timber

interface StockPriceDataSource {
    val latestStockList: Flow<List<Stock>>
}

class NetworkStockPriceDataSource(mockApi: FlowMockApi) : StockPriceDataSource {

    private val TAG = NetworkStockPriceDataSource::class.java.name
    private var attempts = 0L

    override val latestStockList: Flow<List<Stock>> = flow {
        while (true) {
            Timber.tag(TAG).d("Fetching current stock prices")
            val currentStockList = mockApi.getCurrentStockPrices()
            emit(currentStockList)
            delay(5_000)
        }
    }.retryWhen { cause: Throwable, attempt: Long ->
        attempts = attempt + 1
        Timber.tag(TAG).d("Retrying after: ${cause.message}\nAttempt: ${attempt + 1}")

        val isHttpException = cause is HttpException

        if (isHttpException) delay(5_000)

        isHttpException


    }/*.retry { cause: Throwable ->
            Timber.tag(TAG).d("Retrying after: ${cause.message}\nCounting...")
            val isHttpException = cause is HttpException

            if (isHttpException) delay(5_000)

            isHttpException
        }*/
}