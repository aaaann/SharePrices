package com.annevonwolffen.shareprices.data

import com.annevonwolffen.shareprices.models.data.CompanyProfileResponse
import com.annevonwolffen.shareprices.models.data.QuoteResponse
import com.annevonwolffen.shareprices.models.data.SymbolSearchResponse
import io.reactivex.Maybe
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @author Terekhova Anna
 */
interface StocksDataService {

    @GET("quote?token=$API_KEY")
    fun getQuoteForTicker(
        @Query("symbol") symbol: String
    ): Maybe<QuoteResponse>

    @GET("stock/profile2?token=$API_KEY")
    fun getCompanyProfile(
        @Query("symbol") symbol: String
    ): Maybe<CompanyProfileResponse>

    @GET("search?token=$API_KEY")
    fun searchSymbol(
        @Query("q") query: String
    ): Maybe<SymbolSearchResponse>

    companion object {
        const val BASE_URL = "https://finnhub.io/api/v1/"
        private const val API_KEY = "c11rphv48v6p2grlkfbg"
    }
}