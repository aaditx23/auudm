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

    fun updateSelectedMonths(months: List<Int>) {
        _uiState.value = _uiState.value.copy(selectedMonths = months, monthError = false)
    }

    fun toggleMonth(month: Int) {
        val current = _uiState.value.selectedMonths
        val new = if (current.contains(month)) {
            current - month
        } else {
            current + month
        }
        _uiState.value = _uiState.value.copy(selectedMonths = new, monthError = false)
    }

    fun updateAmount(amount: String) {
        _uiState.value = _uiState.value.copy(amount = amount, amountError = false)
    }

    fun updateSelectedRecipient(recipient: String) {
        _uiState.value = _uiState.value.copy(selectedRecipient = recipient)
    }

    fun updateSelectedMedium(medium: String) {
        _uiState.value = _uiState.value.copy(selectedMedium = medium)
    }

    fun updateMediumReference(reference: String) {
        _uiState.value = _uiState.value.copy(mediumReference = reference)
    }

    fun updateSelectedYear(year: Int) {
        _uiState.value = _uiState.value.copy(selectedYear = year)
    }

    fun validateFields(): Boolean {
        val state = _uiState.value
        val donorNameError = state.donorName.isBlank()
        val addressError = state.address.isBlank()
        val amountError = state.amount.isBlank() || state.amount.toDoubleOrNull() == null || state.amount.toDoubleOrNull()!! <= 0
        val recipientError = state.selectedRecipient.isBlank()
        val monthError = state.selectedMonths.isEmpty()

        _uiState.value = state.copy(
            donorNameError = donorNameError,
            addressError = addressError,
            amountError = amountError,
            recipientError = recipientError,
            monthError = monthError
        )

        return !donorNameError && !addressError && !amountError && !recipientError && !monthError
    }

    suspend fun saveReceipt(mediums: List<String>, recipients: List<String>): Receipt {
        val state = _uiState.value
        val receipt = Receipt(
            donorName = state.donorName,
            address = state.address,
            month = state.selectedMonths,
            year = state.selectedYear,
            amount = state.amount.toDoubleOrNull() ?: 0.0,
            recipientIndex = recipients.indexOf(state.selectedRecipient),
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

    fun resetForm(currentMonthInt: Int, defaultMedium: String, defaultRecipient: String) {
        val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        _uiState.value = AddReceiptUiState(
            selectedMonths = listOf(currentMonthInt),
            selectedYear = currentYear,
            selectedMedium = defaultMedium,
            selectedRecipient = defaultRecipient
        )
    }

    fun showDialog(receipt: Receipt, isPreview: Boolean = false) {
        _uiState.value = _uiState.value.copy(showDialog = true, savedReceipt = receipt, isPreview = isPreview)
    }

    fun dismissDialog() {
        _uiState.value = _uiState.value.copy(showDialog = false, savedReceipt = null)
    }
}
