package com.aaditx23.auudm.domain.usecase

import com.aaditx23.auudm.domain.model.Receipt
import com.aaditx23.auudm.domain.repository.ReceiptRepository
import kotlinx.coroutines.flow.Flow

class GetReceiptsUseCase(private val repository: ReceiptRepository) {

    suspend operator fun invoke(): Flow<List<Receipt>> {
        return repository.getReceipts()
    }

}
