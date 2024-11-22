package com.euriconeto.markupcalculator.ui.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.euriconeto.markupcalculator.repository.MarkupRepositoryImpl
import com.euriconeto.markupcalculator.service.ConsultResult
import com.euriconeto.markupcalculator.service.model.Markup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MarkupListViewModel(
    private val markupRepository: MarkupRepositoryImpl
) : ViewModel() {
    private val _markupList = MutableStateFlow(mutableStateOf(listOf<Markup>()))
    val markupList: StateFlow<MutableState<List<Markup>>> = _markupList.asStateFlow()
    private val _validation = MutableStateFlow(mutableStateOf(false))
    val validation: StateFlow<MutableState<Boolean>> = _validation.asStateFlow()
    private val _loading = MutableStateFlow(mutableStateOf(false))
    val loading: StateFlow<MutableState<Boolean>> = _loading.asStateFlow()


    fun initialize() {
        if (_markupList.value.value.isEmpty()) {
            viewModelScope.launch(Dispatchers.IO) { updateList() }
        }
    }

    fun deleteMarkup(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            markupRepository.deleteMarkup(id)
            updateList()
        }
    }

    private suspend fun updateList() {
        markupRepository.getAllMarkups().collect {
            when(it) {
                is ConsultResult.Success -> {
                    _markupList.value.value = it.data
                    _validation.value.value = true
                    _loading.value.value = false
                }
                is ConsultResult.Loading -> {
                    _markupList.value.value = listOf()
                    _validation.value.value = true
                    _loading.value.value = true
                }
                is ConsultResult.Error -> {
                    _markupList.value.value = listOf()
                    _validation.value.value = false
                    _loading.value.value = false
                }
            }
        }
    }
}