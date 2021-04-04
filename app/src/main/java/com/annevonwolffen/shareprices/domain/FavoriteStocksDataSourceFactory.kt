package com.annevonwolffen.shareprices.domain

import androidx.paging.DataSource
import com.annevonwolffen.shareprices.models.presentation.StockPresentationModel
import io.reactivex.disposables.CompositeDisposable

class FavoriteStocksDataSourceFactory(
    stocksInteractor: StocksInteractor,
    compositeDisposable: CompositeDisposable
) : StocksDataSourceFactory(stocksInteractor, compositeDisposable) {

    override fun create(): DataSource<Int, StockPresentationModel> {
        val stocksDataSource = FavoriteStocksDataSource(stocksInteractor, compositeDisposable)
        _stocksDataSourceMLD.postValue(stocksDataSource)
        return stocksDataSource
    }
}