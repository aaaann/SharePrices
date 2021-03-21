package com.annevonwolffen.shareprices.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.annevonwolffen.shareprices.domain.StocksInteractor
import com.annevonwolffen.shareprices.presentation.StocksAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchViewModel(
    stocksInteractor: StocksInteractor
) : BaseStocksViewModel(stocksInteractor), StocksAdapter.OnItemClickListener {

    private val _recentSearchedTickers = MutableLiveData<List<String>>()
    val recentSearchedTickers: LiveData<List<String>> = _recentSearchedTickers

    fun runSearch(query: String) {
        compositeDisposable.add(
            stocksInteractor.searchSymbol(query)
                .doOnSubscribe { _isLoading.postValue(true) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { _isLoading.value = false }
                .subscribe({ _stocks.value = it }, { Log.d(TAG, it?.message.orEmpty()) })
        )
    }

    fun getRecentSearch() {
        compositeDisposable.add(
            stocksInteractor.getRecentSearch()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ _recentSearchedTickers.value = it }, { Log.d(TAG, it?.message.orEmpty()) })
        )
    }

    override fun onItemClicked(ticker: String) {
        Log.d(TAG, "click on stock with ticker $ticker")
        stocksInteractor.addToRecentSearch(ticker)
    }

    private companion object {
        const val TAG = "SearchViewModel"
    }
}