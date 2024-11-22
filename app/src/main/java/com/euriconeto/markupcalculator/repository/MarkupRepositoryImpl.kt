package com.euriconeto.markupcalculator.repository

import com.euriconeto.markupcalculator.service.ConsultResult
import com.euriconeto.markupcalculator.service.db.dao.MarkupDao
import com.euriconeto.markupcalculator.service.model.Markup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MarkupRepositoryImpl(
    private val markupDao: MarkupDao
): MarkupRepository {
    override suspend fun saveMarkup(markup: Markup) {
        markupDao.save(markup)
    }

    override suspend fun getMarkup(id: Int): Flow<ConsultResult<Markup>> = flow {
        emit(ConsultResult.Loading)
        val markup = markupDao.findById(id)
        emit(ConsultResult.Success(markup))
    }

    override suspend fun getAllMarkups(): Flow<ConsultResult<List<Markup>>> = flow {
        emit(ConsultResult.Loading)
        val markups = markupDao.getAll()
        emit(ConsultResult.Success(markups))
    }

    override suspend fun deleteMarkup(id: Int) {
        markupDao.deleteById(id)
    }
}