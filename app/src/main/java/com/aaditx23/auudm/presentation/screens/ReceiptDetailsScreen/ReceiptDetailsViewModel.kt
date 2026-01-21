package com.aaditx23.auudm.presentation.screens.ReceiptDetailsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.auudm.domain.model.Receipt
import com.aaditx23.auudm.domain.usecase.GetReceiptByIdUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class ReceiptDetailsViewModel(
    private val getReceiptByIdUseCase: GetReceiptByIdUseCase,
    private val receiptId: Long
) : ViewModel() {

    val receipt: StateFlow<Receipt?> = flow {
        val f = getReceiptByIdUseCase(receiptId)
        emitAll(f)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
}
