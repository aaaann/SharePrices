package com.annevonwolffen.shareprices.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.annevonwolffen.shareprices.data.RawDataHelper
import com.annevonwolffen.shareprices.data.StocksApiMapperImpl
import com.annevonwolffen.shareprices.data.StocksSharedPrefHelperImpl
import com.annevonwolffen.shareprices.data.converter.CompanyInfoResponseToDomainConverter
import com.annevonwolffen.shareprices.data.database.StocksDatabase
import com.annevonwolffen.shareprices.data.converter.ResponseToStockDomainModelConverter
import com.annevonwolffen.shareprices.data.repository.CompanyInfoRepositoryImpl
import com.annevonwolffen.shareprices.data.repository.StocksRepositoryImpl
import com.annevonwolffen.shareprices.domain.DomainToPresentationModelConverter
import com.annevonwolffen.shareprices.domain.StocksInteractorImpl
import com.annevonwolffen.shareprices.domain.company.CompanyInfoInteractorImpl
import com.annevonwolffen.shareprices.utils.ResourceWrapperImpl

/**
 * @author Terekhova Anna
 */
class ViewModelProviderFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass == StocksViewModel::class.java -> {
                val resourceWrapper = ResourceWrapperImpl(context)
                val database = StocksDatabase.getDatabase(context)
                val stocksRepository = StocksRepositoryImpl(
                    StocksApiMapperImpl(),
                    database.stocksDao(),
                    RawDataHelper(resourceWrapper),
                    ResponseToStockDomainModelConverter()
                )
                val sharedPrefHelper = StocksSharedPrefHelperImpl(context)
                val stocksInteractor =
                    StocksInteractorImpl(
                        stocksRepository,
                        DomainToPresentationModelConverter(resourceWrapper),
                        sharedPrefHelper
                    )
                StocksViewModel(stocksInteractor) as T
            }
            modelClass == SearchViewModel::class.java -> {
                val resourceWrapper = ResourceWrapperImpl(context)
                val database = StocksDatabase.getDatabase(context)
                val stocksRepository = StocksRepositoryImpl(
                    StocksApiMapperImpl(),
                    database.stocksDao(),
                    RawDataHelper(resourceWrapper),
                    ResponseToStockDomainModelConverter()
                )
                val sharedPrefHelper = StocksSharedPrefHelperImpl(context)
                val stocksInteractor =
                    StocksInteractorImpl(
                        stocksRepository,
                        DomainToPresentationModelConverter(resourceWrapper),
                        sharedPrefHelper
                    )
                SearchViewModel(stocksInteractor) as T
            }
            modelClass == StockDetailsViewModel::class.java -> {
                val resourceWrapper = ResourceWrapperImpl(context)
                val database = StocksDatabase.getDatabase(context)
                val stocksRepository = StocksRepositoryImpl(
                    StocksApiMapperImpl(),
                    database.stocksDao(),
                    RawDataHelper(resourceWrapper),
                    ResponseToStockDomainModelConverter()
                )
                val sharedPrefHelper = StocksSharedPrefHelperImpl(context)
                val stocksInteractor =
                    StocksInteractorImpl(
                        stocksRepository,
                        DomainToPresentationModelConverter(resourceWrapper),
                        sharedPrefHelper
                    )

                val companyInfoRepository = CompanyInfoRepositoryImpl(
                    StocksApiMapperImpl(),
                    database.companyProfileDao(),
                    CompanyInfoResponseToDomainConverter()
                )
                val companyInfoInteractor = CompanyInfoInteractorImpl(companyInfoRepository)
                StockDetailsViewModel(companyInfoInteractor, stocksInteractor) as T
            }
            else -> super.create(modelClass)
        }
    }
}