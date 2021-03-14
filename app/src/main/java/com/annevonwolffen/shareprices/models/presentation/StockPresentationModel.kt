package com.annevonwolffen.shareprices.models.presentation

/**
 *
 */
data class StockPresentationModel(
    val ticker: String = "",
    val name: String = "",
    val logo: String = "",
    val currentPrice: String = "",
    val priceChange: String = "",
    val isPriceUp: Boolean = false,
    val isFavorite: Boolean = false
)