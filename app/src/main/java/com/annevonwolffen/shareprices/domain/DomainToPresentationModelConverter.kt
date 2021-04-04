package com.annevonwolffen.shareprices.domain

import com.annevonwolffen.shareprices.R
import com.annevonwolffen.shareprices.models.domain.StockModel
import com.annevonwolffen.shareprices.models.presentation.StockPresentationModel
import com.annevonwolffen.shareprices.utils.ResourceWrapper
import kotlin.math.abs

class DomainToPresentationModelConverter(private val resourceWrapper: ResourceWrapper) {
    fun convertToPresentationModel(stockDomainModel: StockModel, isFavorite: Boolean = true): StockPresentationModel {
        return StockPresentationModel(
            stockDomainModel.ticker,
            stockDomainModel.name,
            stockDomainModel.logo,
            createCurrentPriceString(stockDomainModel.currentPrice).orEmpty(),
            createPriceChangeString(stockDomainModel.currentPrice, stockDomainModel.previousClosePrice).orEmpty(),
            calculatePriceChange(stockDomainModel.currentPrice, stockDomainModel.previousClosePrice) > 0,
            isFavorite
        )
    }

    private fun createCurrentPriceString(currentPrice: Double): String? {
        return resourceWrapper.getString(R.string.current_price_pattern, currentPrice)
    }

    private fun createPriceChangeString(currentPrice: Double, previousPrice: Double): String? {
        val priceChange = calculatePriceChange(currentPrice, previousPrice)
        return resourceWrapper.getString(
            R.string.price_change_up_pattern,
            abs(priceChange),
            priceChangeInPercent(priceChange, previousPrice)
        )
    }

    private fun calculatePriceChange(currentPrice: Double, previousPrice: Double): Double {
        return currentPrice - previousPrice
    }

    private fun priceChangeInPercent(difference: Double, previousPrice: Double): Double {
        return (abs(difference) / previousPrice) * 100
    }
}