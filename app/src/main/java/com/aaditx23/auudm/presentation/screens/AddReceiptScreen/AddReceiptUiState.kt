package com.aaditx23.auudm.presentation.screens.AddReceiptScreen

import com.aaditx23.auudm.domain.model.Receipt

data class AddReceiptUiState(
    val donorName: String = "",
    val address: String = "",
    val selectedMonths: List<Int> = emptyList(),
    val amount: String = "",
    val selectedRecipient: String = "",
    val selectedMedium: String = "",
    val mediumReference: String = "",
    val donorNameError: Boolean = false,
    val addressError: Boolean = false,
    val amountError: Boolean = false,
    val recipientError: Boolean = false,
    val monthError: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDialog: Boolean = false,
    val savedReceipt: Receipt? = null,
    val isPreview: Boolean = false
)
