package com.annevonwolffen.shareprices.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.annevonwolffen.shareprices.domain.StocksInteractor
import com.annevonwolffen.shareprices.models.presentation.StockPresentationModel
import com.annevonwolffen.shareprices.presentation.StocksAdapter
import io.reactivex.disposables.CompositeDisposable

open class BaseStocksViewModel(
    protected val stocksInteractor: StocksInteractor
) : ViewModel(), StocksAdapter.FavoriteClickListener {
    var isNewInstance: Boolean = true

    protected val _stocks = MutableLiveData<List<StockPresentationModel>>()
    val stocks: LiveData<List<StockPresentationModel>> = _stocks

    protected val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    override fun onFavClick(ticker: String) {
        val isFavorite = stocksInteractor.setFavorite(ticker)
        val stocks = _stocks.value?.toMutableList()
        val favoriteStockPos = stocks?.indexOfFirst { it.ticker == ticker }
        favoriteStockPos?.takeIf { it != -1 }?.let {
            val favoriteStock = stocks[it]
            stocks[it] = favoriteStock.copy(isFavorite = isFavorite)
            _stocks.value = stocks
        }
    }
}