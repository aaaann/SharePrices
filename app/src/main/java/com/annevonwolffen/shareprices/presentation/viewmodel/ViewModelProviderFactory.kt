package com.annevonwolffen.shareprices.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.annevonwolffen.shareprices.data.RawDataHelper
import com.annevonwolffen.shareprices.data.StocksApiMapperImpl
import com.annevonwolffen.shareprices.data.converter.ResponseToStockDomainModelConverter
import com.annevonwolffen.shareprices.data.repository.StocksRepositoryImpl
import com.annevonwolffen.shareprices.domain.StocksInteractorImpl
import com.annevonwolffen.shareprices.utils.ResourceWrapperImpl

/**
 * @author Terekhova Anna
 */
class ViewModelProviderFactory(private val context: Context) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass == StocksViewModel::class.java -> {
                val resourceWrapper = ResourceWrapperImpl(context.resources)
                val stocksRepository = StocksRepositoryImpl(
                    StocksApiMapperImpl(),
                    RawDataHelper(resourceWrapper),
                    ResponseToStockDomainModelConverter()
                )
                val stocksInteractor = StocksInteractorImpl(stocksRepository)
                StocksViewModel(stocksInteractor) as T
            }
            else -> super.create(modelClass)
        }
    }
}