package com.annevonwolffen.shareprices.domain

import com.annevonwolffen.shareprices.data.StocksSharedPrefHelper
import com.annevonwolffen.shareprices.models.presentation.StockPresentationModel
import io.reactivex.Single

/**
 * @author Terekhova Anna
 */
class StocksInteractorImpl(
    private val repository: StocksRepository,
    private val converter: DomainToPresentationModelConverter,
    private val sharedPrefHelper: StocksSharedPrefHelper
) : StocksInteractor {
    override fun getPopularStocksData(): Single<List<StockPresentationModel>> = repository.getPopularStocksData()
        .map { list -> list.map { converter.convertToPresentationModel(it, sharedPrefHelper.isFavorite(it.ticker)) } }

    override fun searchSymbol(query: String): Single<List<StockPresentationModel>> = repository.getStocksSearch(query)
        .map { list -> list.map { converter.convertToPresentationModel(it, sharedPrefHelper.isFavorite(it.ticker)) } }

    override fun setFavorite(ticker: String): Boolean = sharedPrefHelper.addFavorite(ticker)

    override fun getRecentSearch(): Single<List<String>> =
        Single.fromCallable { sharedPrefHelper.getRecentSearched().toList() }

    override fun addToRecentSearch(ticker: String) = sharedPrefHelper.addToRecentSearched(ticker)
}