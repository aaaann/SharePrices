package com.annevonwolffen.shareprices.domain

import com.annevonwolffen.shareprices.models.presentation.StockPresentationModel
import io.reactivex.Single

/**
 * @author Terekhova Anna
 */
interface StocksInteractor {
    fun getPopularStocksData(): Single<List<StockPresentationModel>>

    fun setFavorite(ticker: String): Boolean
}