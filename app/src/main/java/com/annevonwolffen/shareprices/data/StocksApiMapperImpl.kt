package com.annevonwolffen.shareprices.data

import com.annevonwolffen.shareprices.models.data.CompanyProfileResponse
import com.annevonwolffen.shareprices.models.data.QuoteResponse
import io.reactivex.Single

/**
 * @author Terekhova Anna
 */
class StocksApiMapperImpl : StocksApiMapper {

    private val stocksService: StocksDataService = StocksApi.stocksService

    override fun getQuoteForTicker(ticker: String): Single<QuoteResponse> {
        return stocksService.getQuoteForTicker(ticker)
    }

    override fun getCompanyProfile(ticker: String): Single<CompanyProfileResponse> {
        return stocksService.getCompanyProfile(ticker)
    }
}