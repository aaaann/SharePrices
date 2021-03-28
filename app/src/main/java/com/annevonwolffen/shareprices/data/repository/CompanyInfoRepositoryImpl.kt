package com.annevonwolffen.shareprices.data.repository

import android.util.Log
import com.annevonwolffen.shareprices.data.StocksApiMapper
import com.annevonwolffen.shareprices.data.converter.CompanyInfoResponseToDomainConverter
import com.annevonwolffen.shareprices.data.database.CompanyProfileDao
import com.annevonwolffen.shareprices.domain.company.CompanyInfoRepository
import com.annevonwolffen.shareprices.models.domain.CompanyInfoModel
import io.reactivex.Maybe
import io.reactivex.Single

class CompanyInfoRepositoryImpl(
    private val apiMapper: StocksApiMapper,
    private val companyProfileDao: CompanyProfileDao,
    private val converter: CompanyInfoResponseToDomainConverter
) : CompanyInfoRepository {
    override fun getCompanyInfo(ticker: String): Single<CompanyInfoModel> {
        return Maybe.fromCallable { getCompanyProfileInfo(ticker) }
            .flatMap { it }
            .doOnSuccess { companyProfileDao.insertCompanyProfile(it) }
            .switchIfEmpty(Single.fromCallable { companyProfileDao.getCompanyProfileByTicker(ticker) })
    }

    private fun getCompanyProfileInfo(ticker: String): Maybe<CompanyInfoModel> {
        return apiMapper.getCompanyProfile(ticker)
            .map { converter.convert(it) }
            .doOnError { t -> Log.e(TAG, t?.message.orEmpty()) }
            .onErrorComplete()
    }

    private companion object {
        const val TAG = "CompanyInfoRepository"
    }
}