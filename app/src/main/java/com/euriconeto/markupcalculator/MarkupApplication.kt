package com.euriconeto.markupcalculator

import android.app.Application
import com.euriconeto.markupcalculator.di.appModule
import com.euriconeto.markupcalculator.di.dataBaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class MarkupApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MarkupApplication)
            modules(
                appModule,
                dataBaseModule
            )
        }
    }
}