package com.annevonwolffen.shareprices.presentation.viewmodel

import com.annevonwolffen.shareprices.domain.FavoriteStocksDataSourceFactory
import com.annevonwolffen.shareprices.domain.StocksDataSourceFactory
import com.annevonwolffen.shareprices.domain.StocksInteractor
import javax.inject.Inject

class FavoriteStocksViewModel @Inject constructor(
    stocksInteractor: StocksInteractor
) : StocksViewModel(stocksInteractor) {

    override fun createDataSourceFactory(): StocksDataSourceFactory {
        return FavoriteStocksDataSourceFactory(stocksInteractor, compositeDisposable)
    }
}