package com.annevonwolffen.shareprices.domain.company

import com.annevonwolffen.shareprices.models.domain.CompanyInfoModel
import io.reactivex.Single

interface CompanyInfoInteractor {
    fun getCompanyInfo(ticker: String): Single<CompanyInfoModel>
}