package com.annevonwolffen.shareprices.domain

import com.annevonwolffen.shareprices.models.domain.StockModel
import io.reactivex.Single

/**
 * @author Terekhova Anna
 */
class StocksInteractorImpl(private val repository: StocksRepository) : StocksInteractor {
    override fun getPopularStocksData(): Single<List<StockModel>> = repository.getPopularStocksData()
}