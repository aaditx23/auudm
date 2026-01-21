package com.aaditx23.auudm.data.repository

import com.aaditx23.auudm.data.local.dao.ReceiptDao
import com.aaditx23.auudm.data.local.mapper.toDomain
import com.aaditx23.auudm.data.local.mapper.toEntity
import com.aaditx23.auudm.data.remote.datasource.FirestoreDataSource
import com.aaditx23.auudm.data.remote.mapper.toDomain as firestoreToDomain
import com.aaditx23.auudm.data.remote.mapper.toDto
import com.aaditx23.auudm.domain.model.Receipt
import com.aaditx23.auudm.domain.repository.ReceiptRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ReceiptRepositoryImpl(
    private val dao: ReceiptDao,
    private val firestoreDataSource: FirestoreDataSource
) : ReceiptRepository {

    override suspend fun saveReceipt(receipt: Receipt): Long {
        val entity = receipt.toEntity()
        val id = dao.insert(entity)

        // Auto-sync to Firestore after saving locally
        val receiptWithId = receipt.copy(id = id)
        syncReceiptToFirestore(receiptWithId)

        return id
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

    // Firestore operations
    override suspend fun syncReceiptToFirestore(receipt: Receipt): Result<Unit> {
        return try {
            firestoreDataSource.saveReceipt(receipt.toDto())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncAllReceiptsToFirestore(): Result<Unit> {
        return try {
            val receipts = dao.getAllReceipts().first()
            val dtos = receipts.map { it.toDomain().toDto() }
            firestoreDataSource.syncReceipts(dtos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteReceiptFromFirestore(id: Long): Result<Unit> {
        return firestoreDataSource.deleteReceipt(id)
    }

    override fun getReceiptsFromFirestore(): Flow<List<Receipt>> {
        return firestoreDataSource.getAllReceipts().map { dtos ->
            dtos.map { it.firestoreToDomain() }
        }
    }

    override suspend fun syncAllFromFirestore(): Result<Unit> {
        return try {
            val receipts = firestoreDataSource.getAllReceipts().first().map { it.firestoreToDomain() }
            // Clear local DB
//            dao.deleteAll()
            // Insert all
            val entities = receipts.map { it.toEntity() }
            dao.insertAll(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
