package com.annevonwolffen.shareprices.domain

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.annevonwolffen.shareprices.models.presentation.StockPresentationModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

open class StocksDataSource(
    protected val stocksInteractor: StocksInteractor,
    protected val compositeDisposable: CompositeDisposable
) : PageKeyedDataSource<Int, StockPresentationModel>() {

    protected val _isInitialLoading = MutableLiveData<Boolean>()
    val isInitialLoading: LiveData<Boolean> = _isInitialLoading

    protected val _isFurtherPageLoading = MutableLiveData<Boolean>()
    val isFurtherPageLoading: LiveData<Boolean> = _isFurtherPageLoading

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, StockPresentationModel>
    ) {
        compositeDisposable.add(
            stocksInteractor.getPopularStocksData(0, params.requestedLoadSize)
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
            stocksInteractor.getPopularStocksData(params.key, params.requestedLoadSize)
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

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, StockPresentationModel>) {
        // do nothing
    }

    private companion object {
        const val TAG = "StocksDataSource"
    }
}