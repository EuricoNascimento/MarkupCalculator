package com.euriconeto.markupcalculator.repository

import com.euriconeto.markupcalculator.service.ConsultResult
import com.euriconeto.markupcalculator.service.model.Markup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface MarkupRepository {
    suspend fun saveMarkup(markup: Markup)

    suspend fun getMarkup(id: Int): Flow<ConsultResult<Markup>>

    suspend fun getAllMarkups(): Flow<ConsultResult<List<Markup>>>

    suspend fun deleteMarkup(id: Int)
}