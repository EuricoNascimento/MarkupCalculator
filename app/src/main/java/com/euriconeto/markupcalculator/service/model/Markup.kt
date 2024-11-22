package com.euriconeto.markupcalculator.service.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@Entity
class Markup(
    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "product_coast")
    val productCoast: Float = 0.0f,

    @ColumnInfo(name = "profit_margin")
    val profitMargin: Float = 0.0f,

    @ColumnInfo(name = "selling_price")
    val sellingPrice: Float = 0.0f,

    @ColumnInfo(name = "markup_multiplier")
    val markupMultiplier: Float = 0.0f,

    @ColumnInfo(name = "markup_divider")
    val markupDivider: Float = 0.0f
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    @ColumnInfo(name = "expenses")
    var expensesJson: String = ""

    fun setExpenses(expenses: Map<Int, Float>) {
        expensesJson = Moshi.Builder().build().adapter(Map::class.java).toJson(expenses)
    }

    fun getExpenses(): Map<Int, Float> {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(Map::class.java, Int::class.javaObjectType, Float::class.javaObjectType)
        val adapter = moshi.adapter<Map<Int, Float>>(type)

        val expenses = adapter.fromJson(expensesJson)
        return expenses ?: mapOf()
    }
}
