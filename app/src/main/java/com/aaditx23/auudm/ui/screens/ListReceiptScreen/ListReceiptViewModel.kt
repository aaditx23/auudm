package com.aaditx23.auudm.ui.screens.ListReceiptScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.auudm.domain.model.Receipt
import com.aaditx23.auudm.domain.usecase.GetReceiptsUseCase
import com.aaditx23.auudm.domain.usecase.SearchReceiptsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ListReceiptViewModel(
    private val getReceiptsUseCase: GetReceiptsUseCase,
    private val searchReceiptsUseCase: SearchReceiptsUseCase
) : ViewModel() {

    private val _receipts = MutableStateFlow<List<Receipt>>(emptyList())
    val receipts: StateFlow<List<Receipt>> = _receipts.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        getReceipts()
    }

    private fun getReceipts() {
        viewModelScope.launch {
            getReceiptsUseCase().collectLatest { receipts ->
                _receipts.value = receipts
            }
        }
    }

    fun searchReceipts(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            getReceipts()
        } else {
            viewModelScope.launch {
                searchReceiptsUseCase(query).collectLatest { receipts ->
                    _receipts.value = receipts
                }
            }
        }
    }
}
