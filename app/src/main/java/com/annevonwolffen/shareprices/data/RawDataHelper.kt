package com.annevonwolffen.shareprices.data

import com.annevonwolffen.shareprices.R
import com.annevonwolffen.shareprices.models.data.SymbolJsonModel
import com.annevonwolffen.shareprices.utils.ResourceWrapper
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @author Terekhova Anna
 */
class RawDataHelper(private val resourceWrapper: ResourceWrapper) {
    val popularTickers: List<SymbolJsonModel> =
        Gson().fromJson(getPopularTickersRawData(), object : TypeToken<List<SymbolJsonModel>>() {}.type)

    private fun getPopularTickersRawData(): String = resourceWrapper.openRawResource(R.raw.s_p_tickers)
}