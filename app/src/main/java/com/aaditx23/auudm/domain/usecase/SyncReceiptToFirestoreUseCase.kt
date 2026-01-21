package com.aaditx23.auudm.domain.usecase

import com.aaditx23.auudm.domain.model.Receipt
import com.aaditx23.auudm.domain.repository.ReceiptRepository

class SyncReceiptToFirestoreUseCase(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(receipt: Receipt): Result<Unit> {
        return repository.syncReceiptToFirestore(receipt)
    }
}
