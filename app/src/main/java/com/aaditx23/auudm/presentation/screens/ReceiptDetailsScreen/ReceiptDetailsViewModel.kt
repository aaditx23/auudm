package com.aaditx23.auudm.presentation.screens.ReceiptDetailsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.auudm.domain.model.Receipt
import com.aaditx23.auudm.domain.repository.ReceiptRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ReceiptDetailsViewModel(
    private val repository: ReceiptRepository,
    private val receiptId: Long
) : ViewModel() {

    val receipt: StateFlow<Receipt?> = repository.getReceiptById(receiptId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
