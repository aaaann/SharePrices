package com.annevonwolffen.shareprices.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.annevonwolffen.shareprices.domain.StocksInteractor
import com.annevonwolffen.shareprices.models.presentation.StockPresentationModel
import com.annevonwolffen.shareprices.presentation.StocksAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * @author Terekhova Anna
 */
class StocksViewModel(
    private val stocksInteractor: StocksInteractor
) : ViewModel(), StocksAdapter.FavoriteClickListener {

    private val _stocks = MutableLiveData<List<StockPresentationModel>>()
    val stocks: LiveData<List<StockPresentationModel>> = _stocks

    private val _shimmerVisibility = MutableLiveData<Boolean>()
    val shimmerVisibility: LiveData<Boolean> = _shimmerVisibility

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun loadData() {
        compositeDisposable.add(
            stocksInteractor.getPopularStocksData()
                .doOnSubscribe { _shimmerVisibility.postValue(true) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { _shimmerVisibility.value = false }
                .subscribe({ _stocks.value = it }, { Log.d(TAG, it?.message.orEmpty()) })
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    override fun onFavClick(ticker: String) {
        Log.d(TAG, "stock $ticker on star clicked")
        val isFavorite = stocksInteractor.setFavorite(ticker)
        val stocks = _stocks.value?.toMutableList()
        val favoriteStockPos = stocks?.indexOfFirst { it.ticker == ticker }
        favoriteStockPos?.takeIf { it != -1 }?.let {
            val favoriteStock = stocks[it]
            stocks[it] = favoriteStock.copy(isFavorite = isFavorite)
            _stocks.value = stocks
        }
    }

    private companion object {
        const val TAG = "StocksViewModel"
    }
}