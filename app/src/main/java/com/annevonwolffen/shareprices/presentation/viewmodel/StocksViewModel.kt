package com.annevonwolffen.shareprices.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.annevonwolffen.shareprices.domain.StocksInteractor
import com.annevonwolffen.shareprices.models.domain.StockModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * @author Terekhova Anna
 */
class StocksViewModel(
    private val stocksInteractor: StocksInteractor
) : ViewModel() {

    private val _stocks = MutableLiveData<List<StockModel>>()
    val stocks: LiveData<List<StockModel>> = _stocks

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun loadData() {
        compositeDisposable.add(
            stocksInteractor.getPopularStocksData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ _stocks.value = it }, { Log.d(TAG, it?.message.orEmpty()) })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    private companion object {
        const val TAG = "StocksViewModel"
    }
}