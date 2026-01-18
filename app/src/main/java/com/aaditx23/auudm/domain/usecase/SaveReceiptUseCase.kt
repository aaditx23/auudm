package com.aaditx23.auudm.domain.usecase

import com.aaditx23.auudm.domain.model.Receipt
import com.aaditx23.auudm.domain.repository.ReceiptRepository

class SaveReceiptUseCase(private val repository: ReceiptRepository) {

    suspend operator fun invoke(receipt: Receipt) {
        repository.saveReceipt(receipt)
    }

}
