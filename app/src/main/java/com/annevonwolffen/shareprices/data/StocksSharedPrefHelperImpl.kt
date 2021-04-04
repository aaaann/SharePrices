package com.annevonwolffen.shareprices.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class StocksSharedPrefHelperImpl(context: Context) : StocksSharedPrefHelper {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(STOCKS_PRICES_PREFS, Context.MODE_PRIVATE)

    override fun isFavorite(ticker: String): Boolean =
        sharedPreferences.getStringSet(FAVS, setOf())?.contains(ticker) ?: false

    override fun addFavorite(ticker: String): Boolean {
        val favorites = sharedPreferences.getStringSet(FAVS, setOf()) ?: setOf()
        val newFavorites: MutableSet<String> = HashSet(favorites)
        val isAlreadyFavorite = newFavorites.contains(ticker)
        if (isAlreadyFavorite) {
            newFavorites.remove(ticker)
        } else {
            newFavorites.add(ticker)
        }
        sharedPreferences.edit { putStringSet(FAVS, newFavorites) }
        return !isAlreadyFavorite
    }

    override fun getFavorites(): Set<String> = sharedPreferences.getStringSet(FAVS, emptySet()) ?: emptySet()

    override fun getRecentSearched(): Set<String> = sharedPreferences.getStringSet(RECENT_SEARCH, setOf()) ?: setOf()

    override fun addToRecentSearched(ticker: String) {
        val searches = sharedPreferences.getStringSet(RECENT_SEARCH, setOf()) ?: setOf()
        val newSearches: MutableSet<String> = HashSet(searches)
        newSearches.add(ticker)
        sharedPreferences.edit { putStringSet(RECENT_SEARCH, newSearches) }
    }

    private companion object {
        const val STOCKS_PRICES_PREFS = "STOCKS_PRICES_PREFS"
        const val FAVS = "FAVS"
        const val RECENT_SEARCH = "RECENT_SEARCH"
    }
}