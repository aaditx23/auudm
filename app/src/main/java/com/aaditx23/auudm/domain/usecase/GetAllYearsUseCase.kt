package com.aaditx23.auudm.domain.usecase

import com.aaditx23.auudm.domain.repository.ReceiptRepository
import kotlinx.coroutines.flow.Flow

class GetAllYearsUseCase(private val repository: ReceiptRepository) {

    operator fun invoke(): Flow<List<Int>> {
        return repository.getAllYears()
    }
}
