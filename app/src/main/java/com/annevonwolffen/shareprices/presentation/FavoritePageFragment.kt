package com.annevonwolffen.shareprices.presentation

import androidx.lifecycle.ViewModelProvider
import com.annevonwolffen.shareprices.presentation.viewmodel.FavoriteStocksViewModel

/**
 * @author Terekhova Anna
 */
class FavoritePageFragment : StocksPageFragment() {

    override fun createViewModel() {
        stocksViewModel = activity?.let {
            ViewModelProvider(it, viewModelProviderFactory)[FavoriteStocksViewModel::class.java]
        } ?: throw Exception("Activity is null")
    }
}