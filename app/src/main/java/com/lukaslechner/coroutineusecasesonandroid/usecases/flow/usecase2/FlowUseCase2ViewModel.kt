package com.lukaslechner.coroutineusecasesonandroid.usecases.flow.usecase2

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.lukaslechner.coroutineusecasesonandroid.base.BaseViewModel
import com.lukaslechner.coroutineusecasesonandroid.usecases.flow.mock.Stock
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import timber.log.Timber

class FlowUseCase2ViewModel(
    stockPriceDataSource: StockPriceDataSource,
    defaultDispatcher: CoroutineDispatcher
) : BaseViewModel<UiState>() {

    private val TAG = FlowUseCase2ViewModel::class.java.simpleName

    /*

    Flow exercise 1 Goals
        1) only update stock list when Alphabet(Google) (stock.name ="Alphabet (Google)") stock price is > 2300$
        2) only show stocks of "United States" (stock.country == "United States")
        3) show the correct rank (stock.rank) within "United States", not the world wide rank
        4) filter out Apple  (stock.name ="Apple") and Microsoft (stock.name ="Microsoft"), so that Google is number one
        5) only show company if it is one of the biggest 10 companies of the "United States" (stock.rank <= 10)
        6) stop flow collection after 10 emissions from the dataSource
        7) log out the number of the current emission so that we can check if flow collection stops after exactly 10 emissions
        8) Perform all flow processing on a background thread

     */
    val currentStockPriceAsLiveData: LiveData<UiState> = stockPriceDataSource
        .latestStockList
        //7-Situation
        .withIndex()
        .onEach { indexedValue ->
            Timber.tag(TAG).d("Processing emission ${indexedValue.index + 1}")
        }
        .map { indexedValue: IndexedValue<List<Stock>> ->
            indexedValue.value
        }
        //6-Situation
        .take(10)
        //1-situation
        .filter { stocks ->
            val googlePrices = stocks.find { stock ->
                stock.name.equals("Alphabet (Google)", ignoreCase = true)
            }?.currentPrice ?: return@filter false

            googlePrices > 2300
        }
        //2-Situation ...
        .map { stocks ->
            stocks.filter { stock ->
                stock.country == "United States"
            }
            stocks.mapIndexed { index, stock ->
                stock.copy(rank = index + 1)
            }
            stocks.filterNot { it.name == "Apple" || it.name == "Microsoft" }
            stocks.filter { stock ->
                stock.rank <= 10
            }
        }
        .map { values ->
            UiState.Success(values) as UiState
        }
        .onStart {
            emit(UiState.Loading)
        }
        .asLiveData(defaultDispatcher)


    /*val currentStockPriceAsLiveData: LiveData<UiState> = stockPriceDataSource
        .latestStockList
        //7-Situation
        .withIndex()
        .onEach { indexedValue ->
            Timber.tag(TAG).d("Processing emission ${indexedValue.index + 1}")
        }
        .map { indexedValue: IndexedValue<List<Stock>> ->
            indexedValue.value
        }
        //6-Situation
        .take(10)
        //1-situation
        .filter { stocks ->
            val googlePrices = stocks.find { stock ->
                stock.name.equals("Alphabet (Google)", ignoreCase = true)
            }?.currentPrice ?: return@filter false

            googlePrices > 2300
        }
        //2-Situation
        .map { stocks ->
            stocks.filter { stock ->
                stock.country == "United States"
            }
        }
        //3-Situation
        .map { stocks ->
            stocks.mapIndexed { index, stock ->
                stock.copy(rank = index + 1)
            }
        }
        //4-Situation
        .map { stocks ->
            stocks.filterNot { it.name == "Apple" || it.name == "Microsoft" }
        }
        //or
        //5-Situation
        *//*.map { stocks ->
            stocks.filter { it.name != "Apple" && it.name != "Microsoft" }
        }*//*
        .map { stocks ->
            stocks.filter { stock ->
                stock.rank <= 10
            }
        }
        .map { values ->
            UiState.Success(values) as UiState
        }
        .onStart {
            emit(UiState.Loading)
        }
        .asLiveData(defaultDispatcher)*/


}