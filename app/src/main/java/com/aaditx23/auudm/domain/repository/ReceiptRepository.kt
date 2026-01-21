package com.aaditx23.auudm.domain.repository

import com.aaditx23.auudm.domain.model.Receipt
import kotlinx.coroutines.flow.Flow

interface ReceiptRepository {

    suspend fun saveReceipt(receipt: Receipt): Long

    fun getReceipts(): Flow<List<Receipt>>

    fun searchReceipts(query: String): Flow<List<Receipt>>

    fun getReceiptById(id: Long): Flow<Receipt>

    // Firestore operations
    suspend fun syncReceiptToFirestore(receipt: Receipt): Result<Unit>

    suspend fun syncAllReceiptsToFirestore(): Result<Unit>

    suspend fun deleteReceiptFromFirestore(id: Long): Result<Unit>

    fun getReceiptsFromFirestore(): Flow<List<Receipt>>
}
