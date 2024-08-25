package com.euriconeto.markupcalculator

import android.app.Application
import com.euriconeto.markupcalculator.ui.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                sharedModule
            )
        }
    }
}