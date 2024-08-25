package com.euriconeto.markupcalculator.ui.di

import com.euriconeto.markupcalculator.ui.viewModel.MarkupViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val sharedModule = module {
    viewModel { MarkupViewModel() }
}