package com.aaditx23.auudm.presentation.screens.SettingsScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.auudm.domain.usecase.GetReceiptsFromFirestoreUseCase
import com.aaditx23.auudm.domain.usecase.SaveReceiptUseCase
import com.aaditx23.auudm.domain.usecase.SyncPendingReceiptsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class FirestoreSyncState(
    val isSyncing: Boolean = false,
    val syncSuccess: Boolean = false,
    val syncError: String? = null
)

class SettingsViewModel(
    private val syncPendingReceiptsUseCase: SyncPendingReceiptsUseCase,
    private val getReceiptsFromFirestoreUseCase: GetReceiptsFromFirestoreUseCase,
    private val saveReceiptUseCase: SaveReceiptUseCase
) : ViewModel() {

    private val _syncState = MutableStateFlow(FirestoreSyncState())
    val syncState: StateFlow<FirestoreSyncState> = _syncState.asStateFlow()

    companion object {
        private const val TAG = "SettingsViewModel"
    }

    /**
     * Complete sync operation:
     * 1. Push pending receipts to Firestore
     * 2. Get all receipts from Firestore
     * 3. Save them to local DB
     */
    fun syncAll() {
        viewModelScope.launch {
            _syncState.value = FirestoreSyncState(isSyncing = true)

            try {
                // Step 1: Push pending receipts to Firestore
                Log.d(TAG, "syncAll: Step 1 - Pushing pending receipts to Firestore")
                val pushResult = syncPendingReceiptsUseCase()

                if (pushResult.isFailure) {
                    Log.e(TAG, "syncAll: Failed to push pending receipts", pushResult.exceptionOrNull())
                    _syncState.value = FirestoreSyncState(
                        isSyncing = false,
                        syncError = "Failed to push pending receipts: ${pushResult.exceptionOrNull()?.message}"
                    )
                    return@launch
                }
                Log.d(TAG, "syncAll: Step 1 completed - Pending receipts pushed")

                // Step 2: Get all receipts from Firestore
                Log.d(TAG, "syncAll: Step 2 - Getting all receipts from Firestore")
                val receipts = getReceiptsFromFirestoreUseCase().first()
                Log.d(TAG, "syncAll: Step 2 completed - Retrieved ${receipts.size} receipts from Firestore")

                // Step 3: Save them to local DB
                Log.d(TAG, "syncAll: Step 3 - Saving receipts to local DB")
                receipts.forEach { receipt ->
                    Log.d(TAG, "syncAll: Saving receipt ID: ${receipt.id}, Donor: ${receipt.donorName}")
                    saveReceiptUseCase(receipt)
                }
                Log.d(TAG, "syncAll: Step 3 completed - All receipts saved to local DB")

                _syncState.value = FirestoreSyncState(
                    isSyncing = false,
                    syncSuccess = true
                )
                Log.d(TAG, "syncAll: Complete sync operation finished successfully")

            } catch (e: Exception) {
                Log.e(TAG, "syncAll: Error during sync operation", e)
                _syncState.value = FirestoreSyncState(
                    isSyncing = false,
                    syncError = e.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun clearSyncState() {
        _syncState.value = FirestoreSyncState()
    }
}
