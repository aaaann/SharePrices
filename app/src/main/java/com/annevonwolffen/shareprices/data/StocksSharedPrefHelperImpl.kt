package com.annevonwolffen.shareprices.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class StocksSharedPrefHelperImpl(context: Context) : StocksSharedPrefHelper {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(STOCKS_PRICES_PREFS, Context.MODE_PRIVATE)

    override fun isFavorite(ticker: String): Boolean = sharedPreferences.getBoolean(ticker, false)

    override fun setFavorite(ticker: String): Boolean {
        val isFavorite = isFavorite(ticker)
        sharedPreferences.edit { putBoolean(ticker, !isFavorite) }
        return isFavorite(ticker)
    }

    private companion object {
        const val STOCKS_PRICES_PREFS = "STOCKS_PRICES_PREFS"
    }
}