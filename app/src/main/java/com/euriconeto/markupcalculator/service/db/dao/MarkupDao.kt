package com.euriconeto.markupcalculator.service.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.euriconeto.markupcalculator.service.model.Markup

@Dao
interface MarkupDao {
    @Query("SELECT * FROM markup")
    fun getAll(): List<Markup>

    @Query("SELECT * FROM markup WHERE id = :id")
    fun findById(id: Int): Markup

    @Insert
    fun save(markup: Markup)

    @Query("DELETE FROM markup WHERE id = :id")
    fun deleteById(id: Int)
}