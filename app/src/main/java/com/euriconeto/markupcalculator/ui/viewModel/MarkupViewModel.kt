package com.euriconeto.markupcalculator.ui.viewModel

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.euriconeto.markupcalculator.R
import com.euriconeto.markupcalculator.repository.MarkupRepositoryImpl
import com.euriconeto.markupcalculator.service.ConsultResult
import com.euriconeto.markupcalculator.service.model.Markup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MarkupViewModel(
    private val markupRepository: MarkupRepositoryImpl
) : ViewModel() {
    private val _productCost = MutableStateFlow(mutableStateOf(""))
    val productCost: StateFlow<MutableState<String>> = _productCost.asStateFlow()
    private val _profitMargin = MutableStateFlow(mutableStateOf(""))
    val profitMargin: StateFlow<MutableState<String>> = _profitMargin.asStateFlow()
    private val _sellingPrice = MutableStateFlow(mutableStateOf(""))
    val sellingPrice: StateFlow<MutableState<String>> = _sellingPrice.asStateFlow()
    private val _mkd = MutableStateFlow(mutableStateOf(""))
    val mkd: StateFlow<MutableState<String>> = _mkd.asStateFlow()
    private val _mkm = MutableStateFlow(mutableStateOf(""))
    val mkm: StateFlow<MutableState<String>> = _mkm.asStateFlow()
    private val _validation = MutableStateFlow(mutableStateOf(false))
    val validation: StateFlow<MutableState<Boolean>> = _validation.asStateFlow()
    private val _loading = MutableStateFlow(mutableStateOf(false))
    val loading: StateFlow<MutableState<Boolean>> = _loading.asStateFlow()

    @SuppressLint("MutableCollectionMutableState")
    private val _expenses = MutableStateFlow(mutableStateOf(mutableMapOf<Int, Float>()))
    val expenses: StateFlow<MutableState<MutableMap<Int, Float>>> = _expenses.asStateFlow()

    fun initialize(markupId: Int?) {
        if (
            markupId != null
            && _sellingPrice.value.value.isEmpty()
            && _mkd.value.value.isEmpty()
            && _mkm.value.value.isEmpty()
        ) {
            viewModelScope.launch(Dispatchers.IO) {
                getMarkup(markupId)
            }
            return
        } else if (
            _sellingPrice.value.value.isEmpty()
            && _mkd.value.value.isEmpty()
            && _mkm.value.value.isEmpty()
        ) {
            _validation.value.value = true
            _loading.value.value = false
            _sellingPrice.value.value = "0.0"
            _mkd.value.value = "0.0"
            _mkm.value.value = "0.0"
        }
    }

    fun setProfitMargin(value: String){
        _profitMargin.value.value = value
    }

    fun setProductCost(value: String){
        _productCost.value.value = value
    }

    fun calculateMarkup(context: Context, productCost: Float, profitMargin: Float) {
        val totalCost = expenses.value.value.values.sum() + profitMargin

        val markupMultiplier = 100 / (100 - totalCost)

        val markupDivisor = 1 - (totalCost / 100)

        if (markupDivisor.isInfinite() || markupMultiplier.isInfinite()) {
            _sellingPrice.value.value = context.getString(R.string.error_message)
            return
        }

        val result = (productCost * markupMultiplier)

        val price = ("%.2f".format(result).replace(',', '.'))

        _sellingPrice.value.value = price

        _mkm.value.value = "%.2f".format(markupMultiplier).replace(',', '.')

        _mkd.value.value = "%.2f".format(markupDivisor).replace(',', '.')
    }

    fun updateMarkup(index: Int, value: Float) {
        _expenses.value.value[index] = value
    }

    fun saveMarkup(name: String, profitMargin: Float, productCost: Float) {
        val markup = Markup(
            name = name,
            profitMargin = profitMargin,
            sellingPrice = sellingPrice.value.value.toFloat(),
            markupDivider = mkd.value.value.toFloat(),
            markupMultiplier = mkm.value.value.toFloat(),
            productCoast = productCost
        )

        if (expenses.value.value.isNotEmpty()) {
            markup.setExpenses(expenses.value.value)
        }

        viewModelScope.launch(Dispatchers.IO) {
            markupRepository.saveMarkup(markup)
        }
    }

    private suspend fun getMarkup(id: Int) {
        markupRepository.getMarkup(id).collect {
            when(it) {
                is ConsultResult.Success -> {
                    setMarkup(it.data)
                    _validation.value.value = true
                    _loading.value.value = false
                }
                is ConsultResult.Loading -> {
                    _validation.value.value = true
                    _loading.value.value = true
                }
                is ConsultResult.Error -> {
                    _validation.value.value = false
                    _loading.value.value = false
                }
            }
        }
    }

    private fun setMarkup(markup: Markup) {
        _sellingPrice.value.value = markup.sellingPrice.toString()
        _mkd.value.value = markup.markupDivider.toString()
        _mkm.value.value = markup.markupMultiplier.toString()
        _productCost.value.value = markup.productCoast.toString()
        _profitMargin.value.value = markup.profitMargin.toString()
        _expenses.value.value = markup.getExpenses() as MutableMap<Int, Float>
    }
}