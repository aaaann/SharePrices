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
import com.annevonwolffen.shareprices.utils.safeSubList
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

    override fun getPopularStocksData(startPosition: Int, loadSize: Int): Single<List<StockModel>> {
        return getStocksData(
            getPopularTickers(startPosition, startPosition + loadSize).map { it.symbol }
        )
    }

    override fun getFavoriteStocksData(
        startPosition: Int,
        loadSize: Int,
        favoriteTickers: List<String>
    ): Single<List<StockModel>> {
        return getStocksData(favoriteTickers.safeSubList(startPosition, startPosition + loadSize))
    }

    override fun getStocksSearch(query: String): Single<List<StockModel>> {
        val tickersList = mutableListOf<String>()
        return getSearchSymbols(query)
            .flatMapSingle { list ->
                Observable.fromIterable(list)
                    .doOnNext { Log.d(TAG, "found symbol is $it") }
                    .flatMapMaybe { symbol ->
                        Maybe.fromCallable {
                            getStockModel(symbol)
                                .doOnComplete { tickersList.add(symbol) }
                        }
                            .subscribeOn(Schedulers.io())
                    }
                    .flatMapMaybe { maybe -> maybe }
                    .toList()
                    .doOnSuccess { stocksDao.insert(it) }
                    .map {
                        if (tickersList.isNotEmpty()) it.addAll(stocksDao.selectStocksByTickers(tickersList))
                        return@map it
                    }
                    .doOnError { t -> Log.e(TAG, t?.message.orEmpty()) }
                    .onErrorReturnItem(Collections.emptyList())
            }
    }

    private fun getStocksData(tickers: List<String>): Single<List<StockModel>> {
        val tickersList = mutableListOf<String>()
        return Observable.fromIterable(tickers)
            .flatMapMaybe { symbol ->
                Maybe.fromCallable {
                    getStockModel(symbol)
                        .doOnComplete { tickersList.add(symbol) }
                }
                    .subscribeOn(Schedulers.io())
            }
            .flatMapMaybe { maybe -> maybe }
            .toList()
            .doOnSuccess { stocksDao.insert(it) }
            .map {
                if (tickersList.isNotEmpty()) it.addAll(stocksDao.selectStocksByTickers(tickersList))
                return@map it
            }
            .doOnError { t -> Log.e(TAG, t?.message.orEmpty()) }
            .onErrorReturnItem(Collections.emptyList())
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
            .onErrorReturnItem(Collections.emptyList())
    }

    private fun getPopularTickers(startPosition: Int, endPosition: Int): List<SymbolJsonModel> {
        return rawDataHelper.popularTickers.safeSubList(startPosition, endPosition)
    }

    private companion object {
        const val TAG = "StocksRepository"
        const val LOAD_TIMEOUT = 30L
    }
}