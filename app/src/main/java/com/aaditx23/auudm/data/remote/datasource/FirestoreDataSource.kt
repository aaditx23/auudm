package com.aaditx23.auudm.data.remote.datasource

import com.aaditx23.auudm.data.remote.model.ReceiptDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreDataSource(
    private val firestore: FirebaseFirestore
) {
    companion object {
        private const val RECEIPTS_COLLECTION = "receipts"
    }

    /**
     * Save or update a receipt in Firestore
     * @param receipt The receipt to save
     * @return Result indicating success or failure
     */
    suspend fun saveReceipt(receipt: ReceiptDto): Result<Unit> {
        return try {
            firestore.collection(RECEIPTS_COLLECTION)
                .document(receipt.id.toString())
                .set(receipt)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get all receipts from Firestore
     * @return Flow of list of receipts
     */
    fun getAllReceipts(): Flow<List<ReceiptDto>> = callbackFlow {
        val subscription = firestore.collection(RECEIPTS_COLLECTION)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val receipts = snapshot?.documents?.mapNotNull { document ->
                    try {
                        document.toObject(ReceiptDto::class.java)
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()

                trySend(receipts)
            }

        awaitClose { subscription.remove() }
    }

    /**
     * Get a specific receipt by ID
     * @param id The receipt ID
     * @return Result containing the receipt or error
     */
    suspend fun getReceiptById(id: Long): Result<ReceiptDto> {
        return try {
            val document = firestore.collection(RECEIPTS_COLLECTION)
                .document(id.toString())
                .get()
                .await()

            val receipt = document.toObject(ReceiptDto::class.java)
            if (receipt != null) {
                Result.success(receipt)
            } else {
                Result.failure(Exception("Receipt not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Delete a receipt from Firestore
     * @param id The receipt ID to delete
     * @return Result indicating success or failure
     */
    suspend fun deleteReceipt(id: Long): Result<Unit> {
        return try {
            firestore.collection(RECEIPTS_COLLECTION)
                .document(id.toString())
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Sync all local receipts to Firestore
     * @param receipts List of receipts to sync
     * @return Result indicating success or failure
     */
    suspend fun syncReceipts(receipts: List<ReceiptDto>): Result<Unit> {
        return try {
            val batch = firestore.batch()
            
            receipts.forEach { receipt ->
                val docRef = firestore.collection(RECEIPTS_COLLECTION)
                    .document(receipt.id.toString())
                batch.set(docRef, receipt)
            }
            
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Clear all receipts from Firestore (use with caution)
     * @return Result indicating success or failure
     */
    suspend fun clearAllReceipts(): Result<Unit> {
        return try {
            val documents = firestore.collection(RECEIPTS_COLLECTION)
                .get()
                .await()

            val batch = firestore.batch()
            documents.forEach { document ->
                batch.delete(document.reference)
            }
            
            batch.commit().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

