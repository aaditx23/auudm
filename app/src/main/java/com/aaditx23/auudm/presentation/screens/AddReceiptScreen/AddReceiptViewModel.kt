package com.aaditx23.auudm.presentation.screens.AddReceiptScreen

import androidx.lifecycle.ViewModel
import com.aaditx23.auudm.data.NetworkMonitor
import com.aaditx23.auudm.domain.model.Receipt
import com.aaditx23.auudm.domain.usecase.SaveReceiptUseCase
import com.aaditx23.auudm.domain.usecase.SyncReceiptToFirestoreUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddReceiptViewModel(
    private val saveReceiptUseCase: SaveReceiptUseCase,
    private val syncReceiptToFirestoreUseCase: SyncReceiptToFirestoreUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddReceiptUiState())
    val uiState: StateFlow<AddReceiptUiState> = _uiState.asStateFlow()

    fun updateDonorName(name: String) {
        _uiState.value = _uiState.value.copy(donorName = name, donorNameError = false)
    }

    fun updateAddress(address: String) {
        _uiState.value = _uiState.value.copy(address = address, addressError = false)
    }

    fun updateSelectedMonth(month: String) {
        _uiState.value = _uiState.value.copy(selectedMonth = month)
    }

    fun updateAmount(amount: String) {
        _uiState.value = _uiState.value.copy(amount = amount, amountError = false)
    }

    fun updateRecipientName(name: String) {
        _uiState.value = _uiState.value.copy(recipientName = name, recipientNameError = false)
    }

    fun updateRecipientDesignation(designation: String) {
        _uiState.value = _uiState.value.copy(recipientDesignation = designation, recipientDesignationError = false)
    }

    fun updateSelectedMedium(medium: String) {
        _uiState.value = _uiState.value.copy(selectedMedium = medium)
    }

    fun updateMediumReference(reference: String) {
        _uiState.value = _uiState.value.copy(mediumReference = reference)
    }

    fun validateFields(): Boolean {
        val state = _uiState.value
        val donorNameError = state.donorName.isBlank()
        val addressError = state.address.isBlank()
        val amountError = state.amount.isBlank() || state.amount.toDoubleOrNull() == null || state.amount.toDoubleOrNull()!! <= 0
        val recipientNameError = state.recipientName.isBlank()
        val recipientDesignationError = state.recipientDesignation.isBlank()

        _uiState.value = state.copy(
            donorNameError = donorNameError,
            addressError = addressError,
            amountError = amountError,
            recipientNameError = recipientNameError,
            recipientDesignationError = recipientDesignationError
        )

        return !donorNameError && !addressError && !amountError && !recipientNameError && !recipientDesignationError
    }

    suspend fun saveReceipt(months: List<String>, mediums: List<String>): Receipt {
        val state = _uiState.value
        val receipt = Receipt(
            donorName = state.donorName,
            address = state.address,
            month = months.indexOf(state.selectedMonth) + 1,
            amount = state.amount.toDoubleOrNull() ?: 0.0,
            recipientName = state.recipientName,
            recipientDesignation = state.recipientDesignation,
            medium = mediums.indexOf(state.selectedMedium) + 1,
            mediumReference = state.mediumReference,
            date = System.currentTimeMillis()
        )
        _uiState.value = state.copy(isLoading = true, error = null)
        return try {
            val saved = saveReceiptUseCase(receipt)
            if (networkMonitor.isNetworkAvailable()) {
                syncReceiptToFirestoreUseCase(saved)
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
            saved
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            throw e
        }
    }

    fun resetForm(currentMonth: String, defaultMedium: String) {
        _uiState.value = AddReceiptUiState(
            selectedMonth = currentMonth,
            selectedMedium = defaultMedium
        )
    }

    fun previewReceipt(months: List<String>, mediums: List<String>): Receipt {
        val state = _uiState.value
        return Receipt(
            donorName = state.donorName,
            address = state.address,
            month = months.indexOf(state.selectedMonth) + 1,
            amount = state.amount.toDoubleOrNull() ?: 0.0,
            recipientName = state.recipientName,
            recipientDesignation = state.recipientDesignation,
            medium = mediums.indexOf(state.selectedMedium) + 1,
            mediumReference = state.mediumReference,
            date = System.currentTimeMillis()
        )
    }

    fun showDialog(receipt: Receipt, isPreview: Boolean = false) {
        _uiState.value = _uiState.value.copy(showDialog = true, savedReceipt = receipt, isPreview = isPreview)
    }

    fun dismissDialog() {
        _uiState.value = _uiState.value.copy(showDialog = false, savedReceipt = null)
    }
}
