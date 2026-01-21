package com.aaditx23.auudm.domain.usecase

import com.aaditx23.auudm.domain.repository.ReceiptRepository

class SyncAllReceiptsToFirestoreUseCase(
    private val repository: ReceiptRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.syncAllReceiptsToFirestore()
    }
}
