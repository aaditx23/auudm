package com.aaditx23.auudm.presentation.screens.ListReceiptScreen

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


class ReceiptListViewModel(
    private val getReceiptsUseCase: GetReceiptsUseCase,
    private val searchReceiptsUseCase: SearchReceiptsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReceiptListUiState())
    val uiState: StateFlow<ReceiptListUiState> = _uiState.asStateFlow()

    init {
        getReceipts()
    }

    private fun getReceipts() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                getReceiptsUseCase().collectLatest { receipts ->
                    _uiState.value = _uiState.value.copy(receipts = receipts, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun searchReceipts(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query, isLoading = true, error = null)
        if (query.isBlank()) {
            getReceipts()
        } else {
            viewModelScope.launch {
                try {
                    searchReceiptsUseCase(query).collectLatest { receipts ->
                        _uiState.value = _uiState.value.copy(receipts = receipts, isLoading = false)
                    }
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
            }
        }
    }
}
