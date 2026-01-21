package com.aaditx23.auudm.domain.usecase

import com.aaditx23.auudm.domain.repository.ReceiptRepository
import kotlinx.coroutines.flow.first

class SyncPendingReceiptsUseCase(private val repository: ReceiptRepository) {

    suspend operator fun invoke(): Result<Unit> {
        return try {
            val unsynced = repository.getUnsyncedReceipts().first()
            for (receipt in unsynced) {
                repository.syncReceiptToFirestore(receipt).onSuccess {
                    repository.updateSyncStatus(receipt.id, true)
                }
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
