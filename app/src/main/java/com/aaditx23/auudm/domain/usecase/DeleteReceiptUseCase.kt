package com.aaditx23.auudm.domain.usecase

import com.aaditx23.auudm.domain.repository.ReceiptRepository

class DeleteReceiptUseCase(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return repository.deleteReceipt(id)
    }
}

