package com.annevonwolffen.shareprices.domain

import android.util.Log
import com.annevonwolffen.shareprices.models.presentation.StockPresentationModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FavoriteStocksDataSource(
    stocksInteractor: StocksInteractor,
    compositeDisposable: CompositeDisposable
) : StocksDataSource(stocksInteractor, compositeDisposable) {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, StockPresentationModel>
    ) {
        compositeDisposable.add(
            stocksInteractor.getFavoriteStocksData(0, params.requestedLoadSize)
                .doOnSubscribe { _isInitialLoading.postValue(true) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { _isInitialLoading.value = false }
                .subscribe(
                    { callback.onResult(it, null, params.requestedLoadSize) },
                    { Log.d(TAG, it?.message.orEmpty()) })
        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, StockPresentationModel>) {
        compositeDisposable.add(
            stocksInteractor.getFavoriteStocksData(params.key, params.requestedLoadSize)
                .doOnSubscribe { _isFurtherPageLoading.postValue(true) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { _isFurtherPageLoading.value = false }
                .subscribe({ data ->
                    _isInitialLoading.value?.takeIf { isLoading -> !isLoading }
                        ?.let { callback.onResult(data, params.key + params.requestedLoadSize) }
                },
                    { Log.d(TAG, it?.message.orEmpty()) })
        )
    }

    private companion object {
        const val TAG = "FavStocksDataSource"
    }
}