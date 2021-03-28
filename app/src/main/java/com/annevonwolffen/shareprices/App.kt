package com.annevonwolffen.shareprices

import android.app.Application
import com.annevonwolffen.shareprices.di.AppComponent
import com.annevonwolffen.shareprices.di.DaggerAppComponent

class App : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}