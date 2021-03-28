package com.annevonwolffen.shareprices.data.repository

import android.util.Log
import com.annevonwolffen.shareprices.data.RawDataHelper
import com.annevonwolffen.shareprices.data.StocksApiMapper
import com.annevonwolffen.shareprices.data.database.StocksDao
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
import java.util.concurrent.TimeUnit

/**
 * @author Terekhova Anna
 */
class StocksRepositoryImpl(
    private val apiMapper: StocksApiMapper,
    private val stocksDao: StocksDao,
    private val rawDataHelper: RawDataHelper,
    private val converter: ResponseToStockDomainModelConverter
) : StocksRepository {

    private val tickersList = mutableListOf<String>()

    override fun getPopularStocksData(): Single<List<StockModel>> {
        return Observable.fromIterable(getPopularTickers().subList(0, 21))
            .doOnNext { tickersList.add(it.symbol) }
            .flatMapMaybe { symbolModel ->
                Maybe.fromCallable { getStockModel(symbolModel.symbol) }
                    .subscribeOn(Schedulers.io())
            }
            .flatMapMaybe { maybe -> maybe }
            .toList()
            .doOnSuccess { stocksDao.insert(it) }
            .map { if (it.isEmpty()) stocksDao.selectStocksByTickers(tickersList) else it }
            .map { it.sortedByDescending { stock -> stock.currentPrice } }
            .doOnError { t -> Log.e(TAG, t?.message.orEmpty()) }
            .onErrorReturnItem(Collections.emptyList())
    }

    override fun getStocksSearch(query: String): Single<List<StockModel>> {
        return getSearchSymbols(query)
            .flatMapSingle { list ->
                Observable.fromIterable(list)
                    .doOnNext { Log.d(TAG, "found symbol is $it") }
                    .flatMapMaybe { symbol ->
                        Maybe.fromCallable { getStockModel(symbol) }
                            .subscribeOn(Schedulers.io())
                    }
                    .flatMapMaybe { maybe -> maybe }
                    .toList()
                    .doOnError { t -> Log.e(TAG, t?.message.orEmpty()) }
                    .onErrorReturnItem(Collections.emptyList())
            }
    }

    private fun getStockModel(symbol: String): Maybe<StockModel> {
        return apiMapper.getCompanyProfile(symbol)
            .zipWith(apiMapper.getQuoteForTicker(symbol),
                BiFunction { profile: CompanyProfileResponse, quote: QuoteResponse ->
                    converter.convert(
                        profile,
                        quote
                    )
                }
            )
            .timeout(LOAD_TIMEOUT, TimeUnit.SECONDS)
            .doOnError { t -> Log.e(TAG, t?.message.orEmpty()) }
            .onErrorComplete()
    }

    private fun getSearchSymbols(query: String): Maybe<List<String>> {
        return apiMapper.searchSymbol(query)
            .map { searchResponse ->
                searchResponse.result
                    .map { it.symbol }
            }
            .doOnError { t -> Log.e(TAG, t?.message.orEmpty()) }
            .onErrorComplete()
    }

    private fun getPopularTickers(): List<SymbolJsonModel> = rawDataHelper.popularTickers

    private companion object {
        const val TAG = "StocksRepository"
        const val LOAD_TIMEOUT = 30L
    }
}