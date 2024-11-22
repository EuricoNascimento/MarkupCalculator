package com.euriconeto.markupcalculator.di

import androidx.room.Room
import com.euriconeto.markupcalculator.repository.MarkupRepository
import com.euriconeto.markupcalculator.repository.MarkupRepositoryImpl
import com.euriconeto.markupcalculator.service.db.AppDatabase
import com.euriconeto.markupcalculator.ui.viewModel.MarkupListViewModel
import com.euriconeto.markupcalculator.ui.viewModel.MarkupViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::MarkupViewModel)
    viewModelOf(::MarkupListViewModel)
}

val dataBaseModule = module {
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = AppDatabase::class.java,
            name = "markup_calculator"
        ).build()
    }

    single {
        get<AppDatabase>().markupDao()
    }

    singleOf(::MarkupRepositoryImpl) { bind<MarkupRepository>() }
}