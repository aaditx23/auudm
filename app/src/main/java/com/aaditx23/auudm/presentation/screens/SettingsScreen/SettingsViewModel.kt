package com.aaditx23.auudm.presentation.screens.SettingsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.auudm.domain.usecase.SyncAllReceiptsToFirestoreUseCase
import com.aaditx23.auudm.domain.usecase.SyncPendingReceiptsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FirestoreSyncState(
    val isSyncing: Boolean = false,
    val syncSuccess: Boolean = false,
    val syncError: String? = null
)

class SettingsViewModel(
    private val syncAllReceiptsUseCase: SyncAllReceiptsToFirestoreUseCase,
    private val syncPendingReceiptsUseCase: SyncPendingReceiptsUseCase
) : ViewModel() {

    private val _syncState = MutableStateFlow(FirestoreSyncState())
    val syncState: StateFlow<FirestoreSyncState> = _syncState.asStateFlow()

    internal var lastSyncType = ""

    fun syncAllToFirestore() {
        lastSyncType = "all"
        viewModelScope.launch {
            _syncState.value = FirestoreSyncState(isSyncing = true)

            val result = syncAllReceiptsUseCase()

            result.onSuccess {
                _syncState.value = FirestoreSyncState(
                    isSyncing = false,
                    syncSuccess = true
                )
            }.onFailure { error ->
                _syncState.value = FirestoreSyncState(
                    isSyncing = false,
                    syncError = error.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun syncPendingToFirestore() {
        lastSyncType = "pending"
        viewModelScope.launch {
            _syncState.value = FirestoreSyncState(isSyncing = true)

            val result = syncPendingReceiptsUseCase()

            result.onSuccess {
                _syncState.value = FirestoreSyncState(
                    isSyncing = false,
                    syncSuccess = true
                )
            }.onFailure { error ->
                _syncState.value = FirestoreSyncState(
                    isSyncing = false,
                    syncError = error.message ?: "Unknown error occurred"
                )
            }
        }
    }

    fun clearSyncState() {
        _syncState.value = FirestoreSyncState()
    }
}
