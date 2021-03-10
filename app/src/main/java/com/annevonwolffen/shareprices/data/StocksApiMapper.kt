package com.annevonwolffen.shareprices.data

import com.annevonwolffen.shareprices.models.data.CompanyProfileResponse
import com.annevonwolffen.shareprices.models.data.QuoteResponse
import io.reactivex.Single

/**
 * @author Terekhova Anna
 */
interface StocksApiMapper {
    fun getQuoteForTicker(ticker: String): Single<QuoteResponse>

    fun getCompanyProfile(ticker: String): Single<CompanyProfileResponse>
}