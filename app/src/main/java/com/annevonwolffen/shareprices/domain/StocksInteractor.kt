package com.annevonwolffen.shareprices.domain

import com.annevonwolffen.shareprices.models.domain.StockModel
import io.reactivex.Single

/**
 * @author Terekhova Anna
 */
interface StocksInteractor {
    fun getPopularStocksData(): Single<List<StockModel>>
}