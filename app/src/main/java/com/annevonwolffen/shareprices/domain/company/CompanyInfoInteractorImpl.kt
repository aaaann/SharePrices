package com.annevonwolffen.shareprices.domain.company

import com.annevonwolffen.shareprices.models.domain.CompanyInfoModel
import io.reactivex.Single

class CompanyInfoInteractorImpl(
    private val companyInfoRepository: CompanyInfoRepository
) : CompanyInfoInteractor {
    override fun getCompanyInfo(ticker: String): Single<CompanyInfoModel> {
        return companyInfoRepository.getCompanyInfo(ticker)
    }
}