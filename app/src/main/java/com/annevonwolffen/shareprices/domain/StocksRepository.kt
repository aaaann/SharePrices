package com.annevonwolffen.shareprices.domain

import com.annevonwolffen.shareprices.models.domain.StockModel
import io.reactivex.Single

/**
 * @author Terekhova Anna
 */
interface StocksRepository {
    fun getPopularStocksData(): Single<List<StockModel>>

    fun getStocksSearch(query: String): Single<List<StockModel>>
}