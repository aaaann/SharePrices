package com.annevonwolffen.shareprices.di

import android.content.Context
import com.annevonwolffen.shareprices.di.viewmodel.ViewModelModule
import com.annevonwolffen.shareprices.presentation.FavoritePageFragment
import com.annevonwolffen.shareprices.presentation.SearchActivity
import com.annevonwolffen.shareprices.presentation.StockDetailsActivity
import com.annevonwolffen.shareprices.presentation.StocksPageFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, ViewModelModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(stocksPageFragment: StocksPageFragment)
    fun inject(favoritePageFragment: FavoritePageFragment)
    fun inject(stockDetailsActivity: StockDetailsActivity)
    fun inject(searchActivity: SearchActivity)
}