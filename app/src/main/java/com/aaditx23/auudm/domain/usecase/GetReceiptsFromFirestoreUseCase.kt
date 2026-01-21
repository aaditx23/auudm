package com.aaditx23.auudm.domain.usecase

import com.aaditx23.auudm.domain.model.Receipt
import com.aaditx23.auudm.domain.repository.ReceiptRepository
import kotlinx.coroutines.flow.Flow

class GetReceiptsFromFirestoreUseCase(
    private val repository: ReceiptRepository
) {
    operator fun invoke(): Flow<List<Receipt>> {
        return repository.getReceiptsFromFirestore()
    }
}

