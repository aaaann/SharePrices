package com.annevonwolffen.shareprices.data.converter

import com.annevonwolffen.shareprices.models.data.CompanyProfileResponse
import com.annevonwolffen.shareprices.models.domain.CompanyInfoModel

class CompanyInfoResponseToDomainConverter {
    fun convert(companyProfileResponse: CompanyProfileResponse): CompanyInfoModel {
        return with(companyProfileResponse) {
            CompanyInfoModel(ticker, companyName, phone, logo, webUrl, finnhubIndustry)
        }
    }
}