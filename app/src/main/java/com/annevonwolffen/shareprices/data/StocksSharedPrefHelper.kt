package com.annevonwolffen.shareprices.data

interface StocksSharedPrefHelper {
    fun isFavorite(ticker: String): Boolean

    fun addFavorite(ticker: String): Boolean

    fun getRecentSearched(): Set<String>

    fun addToRecentSearched(ticker: String)
}