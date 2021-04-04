package com.annevonwolffen.shareprices.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.annevonwolffen.shareprices.domain.StocksDataSource
import com.annevonwolffen.shareprices.domain.StocksDataSourceFactory
import com.annevonwolffen.shareprices.domain.StocksInteractor
import com.annevonwolffen.shareprices.models.presentation.StockPresentationModel
import javax.inject.Inject

/**
 * @author Terekhova Anna
 */
open class StocksViewModel @Inject constructor(
    protected val stocksInteractor: StocksInteractor
) : BaseViewModel() {
    private val stocksDataSourceFactory: StocksDataSourceFactory = createDataSourceFactory()
    val stocksPagedList: LiveData<PagedList<StockPresentationModel>>

    init {
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setInitialLoadSizeHint(PAGE_SIZE * 2)
            .setEnablePlaceholders(false)
            .build()
        stocksPagedList = LivePagedListBuilder<Int, StockPresentationModel>(stocksDataSourceFactory, config).build()
    }

    protected open fun createDataSourceFactory() : StocksDataSourceFactory {
        return StocksDataSourceFactory(stocksInteractor, compositeDisposable)
    }

    fun getIsInitialLoading(): LiveData<Boolean> = Transformations.switchMap<StocksDataSource, Boolean>(
        stocksDataSourceFactory.stocksDataSourceLD,
        StocksDataSource::isInitialLoading
    )

    fun getIsFurtherLoading(): LiveData<Boolean> = Transformations.switchMap<StocksDataSource, Boolean>(
        stocksDataSourceFactory.stocksDataSourceLD,
        StocksDataSource::isFurtherPageLoading
    )

    fun invalidateDataSource() =
        stocksDataSourceFactory.stocksDataSourceLD.value?.invalidate()

    fun onFavClick(ticker: String) {
        stocksInteractor.setFavorite(ticker)
        invalidateDataSource()
    }

    protected companion object {
        const val PAGE_SIZE = 4
    }
}