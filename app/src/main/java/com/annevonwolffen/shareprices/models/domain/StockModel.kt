package com.annevonwolffen.shareprices.models.domain

/**
 * @author Terekhova Anna
 */
data class StockModel(
    val ticker: String,
    val name: String,
    val logo: String,
    val currentPrice: Double,
    val previousClosePrice: Double
)
