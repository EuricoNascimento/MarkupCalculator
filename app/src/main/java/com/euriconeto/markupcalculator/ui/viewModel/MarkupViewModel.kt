package com.euriconeto.markupcalculator.ui.viewModel

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.euriconeto.markupcalculator.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MarkupViewModel(): ViewModel() {
    private val markupMap = mutableMapOf<Int, Float>()
    private val _sellingPrice = MutableStateFlow(mutableStateOf(""))
    val sellingPrice: StateFlow<MutableState<String>> = _sellingPrice.asStateFlow()
    private val _mkd = MutableStateFlow(mutableStateOf(""))
    val mkd: StateFlow<MutableState<String>> = _mkd.asStateFlow()
    private val _mkm = MutableStateFlow(mutableStateOf(""))
    val mkm: StateFlow<MutableState<String>> = _mkm.asStateFlow()

    fun calculateMarkup(context: Context, productCost: Float, profitMargin: Float) {
        val totalCost = markupMap.values.sum() + profitMargin

        val markupMultiplier = 100 / (100 - totalCost)

        val markupDivisor = 1 - (totalCost / 100)

        if (markupDivisor.isInfinite() || markupMultiplier.isInfinite()) {
            Toast.makeText(context, context.getString(R.string.error_message), Toast.LENGTH_SHORT).show()
            return
        }

        val result = (productCost * markupMultiplier)

        val price = ("%.2f".format(result).replace(',', '.'))

        _sellingPrice.update {
            it.value = price
            it
        }
        _mkm.update {
            it.value = "%.2f".format(markupMultiplier).replace(',', '.')
            it
        }
        _mkd.update {
            it.value = "%.2f".format(markupDivisor).replace(',', '.')
            it
        }

    }

    fun updateMarkup(index: Int, value: Float) {
        markupMap[index] = value
    }
}