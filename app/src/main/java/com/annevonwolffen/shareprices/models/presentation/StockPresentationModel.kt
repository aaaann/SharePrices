package com.annevonwolffen.shareprices.models.presentation

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *
 */
@Parcelize
data class StockPresentationModel(
    val ticker: String = "",
    val name: String = "",
    val logo: String = "",
    val currentPrice: String = "",
    val priceChange: String = "",
    val isPriceUp: Boolean = false,
    val isFavorite: Boolean = false,
    val positionInList: Int? = null
) : Parcelable {
    fun copyWithChangeListPosition(position: Int): StockPresentationModel {
        return this.copy(positionInList = position)
    }
}