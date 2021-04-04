package com.annevonwolffen.shareprices.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.annevonwolffen.shareprices.models.presentation.StockPresentationModel
import io.reactivex.disposables.CompositeDisposable

open class StocksDataSourceFactory(
    protected val stocksInteractor: StocksInteractor,
    protected val compositeDisposable: CompositeDisposable
) : DataSource.Factory<Int, StockPresentationModel>() {

    protected val _stocksDataSourceMLD = MutableLiveData<StocksDataSource>()
    val stocksDataSourceLD: LiveData<StocksDataSource> = _stocksDataSourceMLD

    override fun create(): DataSource<Int, StockPresentationModel> {
        val stocksDataSource = StocksDataSource(stocksInteractor, compositeDisposable)
        _stocksDataSourceMLD.postValue(stocksDataSource)
        return stocksDataSource
    }
}