package com.aaditx23.auudm.presentation.screens.ReceiptDetailsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.auudm.data.NetworkMonitor
import com.aaditx23.auudm.domain.usecase.DeleteReceiptUseCase
import com.aaditx23.auudm.domain.usecase.GetReceiptByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class ReceiptDetailsViewModel(
    private val getReceiptByIdUseCase: GetReceiptByIdUseCase,
    private val deleteReceiptUseCase: DeleteReceiptUseCase,
    private val networkMonitor: NetworkMonitor,
    private val receiptId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReceiptDetailsUiState())
    val uiState: StateFlow<ReceiptDetailsUiState> = _uiState.asStateFlow()

    init {
        loadReceipt()
    }

    private fun loadReceipt() {
        viewModelScope.launch {
            try {
                val receipt = getReceiptByIdUseCase(receiptId).firstOrNull()
                if (receipt != null) {
                    _uiState.value = _uiState.value.copy(
                        receipt = receipt,
                        isLoading = false,
                        error = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "receipt_not_found"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun showDialog() {
        _uiState.value = _uiState.value.copy(showDialog = true)
    }

    fun dismissDialog() {
        _uiState.value = _uiState.value.copy(showDialog = false)
    }

    fun showDeleteConfirmation() {
        _uiState.value = _uiState.value.copy(showDeleteConfirmation = true)
    }

    fun dismissDeleteConfirmation() {
        _uiState.value = _uiState.value.copy(showDeleteConfirmation = false)
    }

    fun deleteReceipt(onSuccess: () -> Unit) {
        if (!networkMonitor.isNetworkAvailable()) {
            _uiState.value = _uiState.value.copy(
                deleteError = "delete_no_network"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isDeleting = true, deleteError = null)
            try {
                deleteReceiptUseCase(receiptId).onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        showDeleteConfirmation = false
                    )
                    onSuccess()
                }.onFailure { error ->
                    println("delete: ${error.message}")
                    _uiState.value = _uiState.value.copy(
                        isDeleting = false,
                        deleteError = error.message
                    )
                }
            } catch (e: Exception) {
                println("delete : ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isDeleting = false,
                    deleteError = e.message
                )
            }
        }
    }

    fun clearDeleteError() {
        _uiState.value = _uiState.value.copy(deleteError = null)
    }
}
