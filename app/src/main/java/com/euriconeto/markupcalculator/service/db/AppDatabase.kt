package com.euriconeto.markupcalculator.service.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.euriconeto.markupcalculator.service.db.dao.MarkupDao
import com.euriconeto.markupcalculator.service.model.Markup

@Database(entities = [Markup::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun markupDao(): MarkupDao
}