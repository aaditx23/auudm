package com.aaditx23.auudm.presentation.screens.AddReceiptScreen

import com.aaditx23.auudm.domain.model.Receipt

data class AddReceiptUiState(
    val donorName: String = "",
    val address: String = "",
    val selectedMonth: String = "",
    val amount: String = "",
    val recipientName: String = "",
    val recipientDesignation: String = "",
    val selectedMedium: String = "",
    val mediumReference: String = "",
    val donorNameError: Boolean = false,
    val addressError: Boolean = false,
    val amountError: Boolean = false,
    val recipientNameError: Boolean = false,
    val recipientDesignationError: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val showDialog: Boolean = false,
    val savedReceipt: Receipt? = null,
    val isPreview: Boolean = false
)
