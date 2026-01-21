package com.aaditx23.auudm.presentation.screens.AddReceiptScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.auudm.domain.model.Receipt
import com.aaditx23.auudm.domain.usecase.SaveReceiptUseCase
import kotlinx.coroutines.launch

class AddReceiptViewModel(
    private val saveReceiptUseCase: SaveReceiptUseCase
) : ViewModel() {

    suspend fun saveReceipt(receipt: Receipt): Receipt {
        return saveReceiptUseCase(receipt)
    }
}
