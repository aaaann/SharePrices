package com.annevonwolffen.shareprices.data

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @author Terekhova Anna
 */
object StocksApi {
    private const val BASE_URL = "https://finnhub.io/api/v1/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val stocksService: StocksDataService by lazy {
        retrofit.create(StocksDataService::class.java)
    }
}