package com.annevonwolffen.shareprices.data.converter

import com.annevonwolffen.shareprices.models.data.CompanyProfileResponse
import com.annevonwolffen.shareprices.models.data.QuoteResponse
import com.annevonwolffen.shareprices.models.domain.StockModel

/**
 * @author Terekhova Anna
 */
class ResponseToStockDomainModelConverter {
    fun convert(companyProfileResponse: CompanyProfileResponse, quoteResponse: QuoteResponse): StockModel {
        return StockModel(
            companyProfileResponse.ticker,
            companyProfileResponse.companyName,
            companyProfileResponse.logo,
            quoteResponse.currentPrice,
            quoteResponse.previousClosePrice
        )
    }
}