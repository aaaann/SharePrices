package com.annevonwolffen.shareprices.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.annevonwolffen.shareprices.presentation.viewmodel.FavoriteStocksViewModel
import com.annevonwolffen.shareprices.presentation.viewmodel.SearchViewModel
import com.annevonwolffen.shareprices.presentation.viewmodel.StockDetailsViewModel
import com.annevonwolffen.shareprices.presentation.viewmodel.StocksViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(StocksViewModel::class)
    abstract fun provideStocksViewModel(viewModel: StocksViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(StockDetailsViewModel::class)
    abstract fun provideStockDetailsViewModel(viewModel: StockDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteStocksViewModel::class)
    abstract fun provideFavoriteStocksViewModel(viewModel: FavoriteStocksViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    abstract fun provideSearchViewModel(viewModel: SearchViewModel): ViewModel
}