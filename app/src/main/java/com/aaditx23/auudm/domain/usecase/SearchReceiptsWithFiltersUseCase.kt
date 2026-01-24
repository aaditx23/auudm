package com.aaditx23.auudm.domain.usecase

import com.aaditx23.auudm.domain.model.Receipt
import com.aaditx23.auudm.domain.repository.ReceiptRepository
import kotlinx.coroutines.flow.Flow

class SearchReceiptsWithFiltersUseCase(
    private val repository: ReceiptRepository
) {
    operator fun invoke(
        query: String = "",
        month: Int? = null,
        year: Int? = null,
        medium: Int? = null
    ): Flow<List<Receipt>> {
        return repository.searchReceiptsWithFilters(query, month, year, medium)
    }
}
