package com.aaditx23.auudm.domain.repository

import com.aaditx23.auudm.domain.model.Receipt
import kotlinx.coroutines.flow.Flow

interface ReceiptRepository {

    suspend fun saveReceipt(receipt: Receipt): String

    suspend fun deleteReceipt(id: String): Result<Unit>

    fun getReceipts(): Flow<List<Receipt>>

    fun searchReceipts(query: String): Flow<List<Receipt>>

    fun searchReceiptsWithFilters(
        query: String,
        month: Int?,
        year: Int?,
        medium: Int?
    ): Flow<List<Receipt>>

    fun getReceiptById(id: String): Flow<Receipt>

    // Firestore operations
    suspend fun syncReceiptToFirestore(receipt: Receipt): Result<Unit>

    suspend fun syncAllReceiptsToFirestore(): Result<Unit>

    suspend fun deleteReceiptFromFirestore(id: String): Result<Unit>

    fun getReceiptsFromFirestore(): Flow<List<Receipt>>

    suspend fun syncAllFromFirestore(): Result<Unit>

    suspend fun updateSyncStatus(id: String, synced: Boolean): Result<Unit>

    fun getUnsyncedReceipts(): Flow<List<Receipt>>

    fun getAllYears(): Flow<List<Int>>
}
