package com.aaditx23.auudm.ui.screens.AddReceiptScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.auudm.domain.model.Receipt
import com.aaditx23.auudm.domain.usecase.SaveReceiptUseCase
import kotlinx.coroutines.launch

class AddReceiptViewModel(
    private val saveReceiptUseCase: SaveReceiptUseCase
) : ViewModel() {

    fun saveReceipt(receipt: Receipt) {
        viewModelScope.launch {
            saveReceiptUseCase(receipt)
        }
    }
}
