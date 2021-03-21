package com.annevonwolffen.shareprices.presentation.viewmodel

import android.util.Log
import com.annevonwolffen.shareprices.domain.StocksInteractor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author Terekhova Anna
 */
class StocksViewModel(
    stocksInteractor: StocksInteractor
) : BaseStocksViewModel(stocksInteractor) {

    fun loadData() {
        compositeDisposable.add(
            stocksInteractor.getPopularStocksData()
                .doOnSubscribe { _isLoading.postValue(true) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { _isLoading.value = false }
                .subscribe({ _stocks.value = it }, { Log.d(TAG, it?.message.orEmpty()) })
        )
    }

    private companion object {
        const val TAG = "StocksViewModel"
    }
}