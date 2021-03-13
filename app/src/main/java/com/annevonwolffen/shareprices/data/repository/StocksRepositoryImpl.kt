package com.annevonwolffen.shareprices.data.repository

import android.util.Log
import com.annevonwolffen.shareprices.data.RawDataHelper
import com.annevonwolffen.shareprices.data.StocksApiMapper
import com.annevonwolffen.shareprices.data.converter.ResponseToStockDomainModelConverter
import com.annevonwolffen.shareprices.domain.StocksRepository
import com.annevonwolffen.shareprices.models.data.CompanyProfileResponse
import com.annevonwolffen.shareprices.models.data.QuoteResponse
import com.annevonwolffen.shareprices.models.data.SymbolJsonModel
import com.annevonwolffen.shareprices.models.domain.StockModel
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import java.util.Collections

/**
 * @author Terekhova Anna
 */
class StocksRepositoryImpl(
    private val apiMapper: StocksApiMapper,
    private val rawDataHelper: RawDataHelper,
    private val converter: ResponseToStockDomainModelConverter
) : StocksRepository {

    override fun getPopularStocksData(): Single<List<StockModel>> {
        return Observable.fromIterable(getPopularTickers().subList(0, 2))
            .doOnNext { Log.d("StocksRepository", it.symbol) }
            .flatMapMaybe { symbolModel ->
                Maybe.fromCallable {
                    apiMapper.getCompanyProfile(symbolModel.symbol).zipWith(
                        apiMapper.getQuoteForTicker(symbolModel.symbol),
                        BiFunction { profile: CompanyProfileResponse, quote: QuoteResponse ->
                            converter.convert(
                                profile,
                                quote
                            )
                        }
                    )
                }
                    .subscribeOn(Schedulers.io())
                    .doOnError { t -> Log.e(TAG, t?.message.orEmpty()) }
                    .onErrorComplete()
            }
            .flatMapSingle { maybe -> maybe }
            .toList()
            .onErrorReturnItem(Collections.emptyList())
    }

    private fun getPopularTickers(): List<SymbolJsonModel> = rawDataHelper.popularTickers

    private companion object {
        const val TAG = "StocksRepository"
    }
}