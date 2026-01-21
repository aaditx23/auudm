package com.aaditx23.auudm.presentation.screens.ListReceiptScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.auudm.data.NetworkMonitor
import com.aaditx23.auudm.domain.usecase.GetReceiptsUseCase
import com.aaditx23.auudm.domain.usecase.SearchReceiptsUseCase
import com.aaditx23.auudm.domain.usecase.SyncPendingReceiptsUseCase
import com.aaditx23.auudm.domain.usecase.GetReceiptsFromFirestoreUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class ReceiptListViewModel(
    private val getReceiptsUseCase: GetReceiptsUseCase,
    private val searchReceiptsUseCase: SearchReceiptsUseCase,
    private val syncPendingReceiptsUseCase: SyncPendingReceiptsUseCase,
    private val syncAllReceiptsUseCase: SyncPendingReceiptsUseCase,
    private val getReceiptsFromFirestoreUseCase: GetReceiptsFromFirestoreUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReceiptListUiState())
    val uiState: StateFlow<ReceiptListUiState> = _uiState.asStateFlow()

    private var hasSyncedOnNetwork = false

    init {
        getReceipts()
        observeNetwork()
    }

    private fun getReceipts() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                if (networkMonitor.isNetworkAvailable()) {
                    syncAllReceiptsUseCase()
                }
                getReceiptsUseCase().collectLatest { receipts ->
                    _uiState.value = _uiState.value.copy(receipts = receipts, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun searchReceipts(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query, isLoading = true, error = null)
        if (query.isBlank()) {
            getReceipts()
        } else {
            viewModelScope.launch {
                try {
                    searchReceiptsUseCase(query).collectLatest { receipts ->
                        _uiState.value = _uiState.value.copy(receipts = receipts, isLoading = false)
                    }
                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
                }
            }
        }
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            networkMonitor.observeNetworkState().collect { isOnline ->
                if (isOnline && !hasSyncedOnNetwork) {
                    syncReceipts()
                    hasSyncedOnNetwork = true
                } else if (!isOnline) {
                    hasSyncedOnNetwork = false
                }
            }
        }
    }

    private fun syncReceipts() {
        viewModelScope.launch {
            syncPendingReceiptsUseCase().onFailure {
                // Handle push error if needed
            }
            try {
                getReceiptsFromFirestoreUseCase().collect { }
            } catch (e: Exception) {
                // Handle pull error if needed
            }
            // After sync, the local db is updated, and getReceipts() will collect the latest
        }
    }
}
