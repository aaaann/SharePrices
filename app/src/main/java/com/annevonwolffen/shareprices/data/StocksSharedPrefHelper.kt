package com.annevonwolffen.shareprices.data

interface StocksSharedPrefHelper {
    fun isFavorite(ticker: String): Boolean

    fun setFavorite(ticker: String): Boolean
}