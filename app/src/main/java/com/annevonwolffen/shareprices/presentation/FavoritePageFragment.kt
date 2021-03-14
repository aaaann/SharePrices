package com.annevonwolffen.shareprices.presentation

import com.annevonwolffen.shareprices.models.presentation.StockPresentationModel

/**
 * @author Terekhova Anna
 */
class FavoritePageFragment : StocksPageFragment() {

    override fun updateStocksList(stocks: List<StockPresentationModel>) {
        adapter.submitList(stocks.filter { it.isFavorite })
    }
}