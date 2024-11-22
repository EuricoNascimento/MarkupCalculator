package com.euriconeto.markupcalculator.service

sealed class ConsultResult<out R> {
    data class Success<out T>(val data: T) : ConsultResult<T>()
    data class Error(val exception: Throwable) : ConsultResult<Nothing>()
    data object Loading: ConsultResult<Nothing>()
}