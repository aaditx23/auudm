package com.aaditx23.auudm.presentation.screens.ReceiptDetailsScreen

import com.aaditx23.auudm.domain.model.Receipt

data class ReceiptDetailsUiState(
    val receipt: Receipt? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val showDialog: Boolean = false
)
