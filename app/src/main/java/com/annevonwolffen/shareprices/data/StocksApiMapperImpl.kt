package com.annevonwolffen.shareprices.data

import com.annevonwolffen.shareprices.models.data.CompanyProfileResponse
import com.annevonwolffen.shareprices.models.data.QuoteResponse
import io.reactivex.Maybe

/**
 * @author Terekhova Anna
 */
class StocksApiMapperImpl : StocksApiMapper {

    private val stocksService: StocksDataService = StocksApi.stocksService

    override fun getQuoteForTicker(ticker: String): Maybe<QuoteResponse> {
        return stocksService.getQuoteForTicker(ticker)
    }

    override fun getCompanyProfile(ticker: String): Maybe<CompanyProfileResponse> {
        return stocksService.getCompanyProfile(ticker)
    }
}