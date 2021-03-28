package com.annevonwolffen.shareprices.di

import android.content.Context
import androidx.room.Room
import com.annevonwolffen.shareprices.data.RawDataHelper
import com.annevonwolffen.shareprices.data.StocksApiMapper
import com.annevonwolffen.shareprices.data.StocksApiMapperImpl
import com.annevonwolffen.shareprices.data.StocksDataService
import com.annevonwolffen.shareprices.data.StocksDataService.Companion.BASE_URL
import com.annevonwolffen.shareprices.data.StocksSharedPrefHelper
import com.annevonwolffen.shareprices.data.StocksSharedPrefHelperImpl
import com.annevonwolffen.shareprices.data.converter.CompanyInfoResponseToDomainConverter
import com.annevonwolffen.shareprices.data.converter.ResponseToStockDomainModelConverter
import com.annevonwolffen.shareprices.data.database.DB_NAME
import com.annevonwolffen.shareprices.data.database.StocksDatabase
import com.annevonwolffen.shareprices.data.repository.CompanyInfoRepositoryImpl
import com.annevonwolffen.shareprices.data.repository.StocksRepositoryImpl
import com.annevonwolffen.shareprices.domain.DomainToPresentationModelConverter
import com.annevonwolffen.shareprices.domain.StocksInteractor
import com.annevonwolffen.shareprices.domain.StocksInteractorImpl
import com.annevonwolffen.shareprices.domain.company.CompanyInfoInteractor
import com.annevonwolffen.shareprices.domain.company.CompanyInfoInteractorImpl
import com.annevonwolffen.shareprices.utils.GlideImageManager
import com.annevonwolffen.shareprices.utils.ImageManager
import com.annevonwolffen.shareprices.utils.ResourceWrapper
import com.annevonwolffen.shareprices.utils.ResourceWrapperImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideResourceWrapper(context: Context): ResourceWrapper = ResourceWrapperImpl(context)

    @Singleton
    @Provides
    fun provideStocksSharedPrefHelper(context: Context): StocksSharedPrefHelper = StocksSharedPrefHelperImpl(context)

    @Singleton
    @Provides
    fun provideGlideImageManager(context: Context): ImageManager = GlideImageManager(context)

    @Singleton
    @Provides
    fun provideDatabase(context: Context): StocksDatabase {
        return Room.databaseBuilder(
            context,
            StocksDatabase::class.java,
            DB_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideStocksDataService(): StocksDataService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(StocksDataService::class.java)
    }

    @Singleton
    @Provides
    fun provideStocksApiMapper(stocksDataService: StocksDataService): StocksApiMapper =
        StocksApiMapperImpl(stocksDataService)

    @Singleton
    @Provides
    fun provideStocksInteractor(
        stocksApiMapper: StocksApiMapper,
        stocksDatabase: StocksDatabase,
        resourceWrapper: ResourceWrapper,
        stocksSharedPrefHelper: StocksSharedPrefHelper
    ): StocksInteractor {

        val repository = StocksRepositoryImpl(
            stocksApiMapper,
            stocksDatabase.stocksDao(),
            RawDataHelper(resourceWrapper),
            ResponseToStockDomainModelConverter()
        )

        return StocksInteractorImpl(
            repository,
            DomainToPresentationModelConverter(resourceWrapper),
            stocksSharedPrefHelper
        )
    }

    @Singleton
    @Provides
    fun provideCompanyInfoInteractor(
        stocksApiMapper: StocksApiMapper,
        stocksDatabase: StocksDatabase
    ): CompanyInfoInteractor {

        val repository = CompanyInfoRepositoryImpl(
            stocksApiMapper,
            stocksDatabase.companyProfileDao(),
            CompanyInfoResponseToDomainConverter()
        )

        return CompanyInfoInteractorImpl(repository)
    }
}