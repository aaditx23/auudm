package com.aaditx23.auudm.presentation.screens.ListReceiptScreen

import com.aaditx23.auudm.domain.model.Receipt

data class ReceiptListUiState(
    val receipts: List<Receipt> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val isOnline: Boolean = false,
    val isSyncing: Boolean = false,
    val filterMonth: Int? = null, // null means no filter
    val filterMedium: Int? = null, // null means no filter
    val isFilterDialogOpen: Boolean = false
)