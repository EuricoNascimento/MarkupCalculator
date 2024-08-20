package com.euriconeto.markupcalculator.ui.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MarkupViewModel(): ViewModel() {
    private val markupMap = mutableMapOf<Int, Int>()
    private val _sellingPrice = MutableStateFlow(mutableStateOf(""))
    val sellingPrice: StateFlow<MutableState<String>> = _sellingPrice.asStateFlow()
    private val _mkd = MutableStateFlow(mutableStateOf(""))
    val mkd: StateFlow<MutableState<String>> = _mkd.asStateFlow()
    private val _mkm = MutableStateFlow(mutableStateOf(""))
    val mkm: StateFlow<MutableState<String>> = _mkm.asStateFlow()

    fun calculateMarkup(costPrice: Int) {
        val totalCost = markupMap.values.sum()

        val markupDivisor = (100 - totalCost) / 100

        val result = (costPrice * markupDivisor)

        val price = ("%.2f".format(result))

        val markupMultiplier = 1 / markupDivisor

        val confirmResult = (costPrice * markupMultiplier)

        val confirmPrice = ("%.2f".format(confirmResult))

        if (price == confirmPrice) {
            _sellingPrice.value = mutableStateOf(price)
            _mkd.value = mutableStateOf(markupDivisor.toString())
            _mkm.value = mutableStateOf(markupMultiplier.toString())
        }
    }

    fun updateMarkup(index: Int, value: Int) {
        markupMap[index] = value
    }

    fun deleteMarkup(index: Int, value: Int) {
        if (!markupMap.containsKey(index) || markupMap[index] != value) return
        markupMap.remove(index)
    }
}