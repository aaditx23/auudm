package com.aaditx23.auudm.domain.usecase

import com.aaditx23.auudm.domain.model.Receipt
import com.aaditx23.auudm.domain.repository.ReceiptRepository
import kotlinx.coroutines.flow.Flow

class GetReceiptByIdUseCase(private val repository: ReceiptRepository) {

    suspend operator fun invoke(id: Long): Flow<Receipt> {
//        repository.syncAllFromFirestore().getOrThrow()
        return repository.getReceiptById(id)
    }

}
