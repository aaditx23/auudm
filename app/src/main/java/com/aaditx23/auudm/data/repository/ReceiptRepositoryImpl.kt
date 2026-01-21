package com.aaditx23.auudm.data.repository

import com.aaditx23.auudm.data.local.dao.ReceiptDao
import com.aaditx23.auudm.data.local.mapper.toDomain
import com.aaditx23.auudm.data.local.mapper.toEntity
import com.aaditx23.auudm.domain.model.Receipt
import com.aaditx23.auudm.domain.repository.ReceiptRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ReceiptRepositoryImpl(
    private val dao: ReceiptDao
) : ReceiptRepository {

    override suspend fun saveReceipt(receipt: Receipt): Long {
        val entity = receipt.toEntity()
        return dao.insert(entity)
    }

    override fun getReceipts(): Flow<List<Receipt>> {
        return dao.getAllReceipts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchReceipts(query: String): Flow<List<Receipt>> {
        return dao.searchReceipts(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getReceiptById(id: Long): Flow<Receipt> {
        return dao.getReceiptById(id).map { it.toDomain() }
    }
}
