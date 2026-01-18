package com.aaditx23.auudm.domain.repository

import com.aaditx23.auudm.domain.model.Receipt
import kotlinx.coroutines.flow.Flow

interface ReceiptRepository {

    suspend fun saveReceipt(receipt: Receipt)

    fun getReceipts(): Flow<List<Receipt>>

    fun searchReceipts(query: String): Flow<List<Receipt>>

    fun getReceiptById(id: Long): Flow<Receipt>
}
