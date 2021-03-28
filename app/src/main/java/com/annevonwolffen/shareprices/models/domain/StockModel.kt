package com.annevonwolffen.shareprices.models.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Terekhova Anna
 */
@Entity(tableName = "STOCKS")
data class StockModel(
    @PrimaryKey val ticker: String,
    val name: String,
    val logo: String,
    val currentPrice: Double,
    val previousClosePrice: Double
)
