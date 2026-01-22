package com.aaditx23.auudm.presentation.screens.ListReceiptScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aaditx23.auudm.data.NetworkMonitor
import com.aaditx23.auudm.domain.usecase.GetReceiptsUseCase
import com.aaditx23.auudm.domain.usecase.SearchReceiptsUseCase
import com.aaditx23.auudm.domain.usecase.SearchReceiptsWithFiltersUseCase
import com.aaditx23.auudm.domain.usecase.SyncPendingReceiptsUseCase
import com.aaditx23.auudm.domain.usecase.GetReceiptsFromFirestoreUseCase
import com.aaditx23.auudm.domain.usecase.SaveReceiptUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class ReceiptListViewModel(
    private val getReceiptsUseCase: GetReceiptsUseCase,
    private val searchReceiptsUseCase: SearchReceiptsUseCase,
    private val searchReceiptsWithFiltersUseCase: SearchReceiptsWithFiltersUseCase,
    private val syncPendingReceiptsUseCase: SyncPendingReceiptsUseCase,
    private val getReceiptsFromFirestoreUseCase: GetReceiptsFromFirestoreUseCase,
    private val saveReceiptsUseCase: SaveReceiptUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReceiptListUiState())
    val uiState: StateFlow<ReceiptListUiState> = _uiState.asStateFlow()

    init {
        observeNetwork()
        getReceipts()
    }

    private fun getReceipts() {
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                if (networkMonitor.isNetworkAvailable()) {
                    syncFromNetwork()
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
        viewModelScope.launch {
            try {
                // Use searchReceiptsWithFiltersUseCase for all searches (with or without filters)
                searchReceiptsWithFiltersUseCase(
                    query = query,
                    month = _uiState.value.filterMonth,
                    medium = _uiState.value.filterMedium
                ).collectLatest { receipts ->
                    _uiState.value = _uiState.value.copy(receipts = receipts, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun applyFilters(month: Int?, medium: Int?) {
        _uiState.value = _uiState.value.copy(
            filterMonth = month,
            filterMedium = medium,
            isLoading = true
        )
        // Reapply search with new filters
        viewModelScope.launch {
            try {
                searchReceiptsWithFiltersUseCase(
                    query = _uiState.value.searchQuery,
                    month = month,
                    medium = medium
                ).collectLatest { receipts ->
                    _uiState.value = _uiState.value.copy(receipts = receipts, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun clearFilters() {
        _uiState.value = _uiState.value.copy(
            filterMonth = null,
            filterMedium = null,
            isLoading = true
        )
        // Reapply search without filters
        viewModelScope.launch {
            try {
                searchReceiptsWithFiltersUseCase(
                    query = _uiState.value.searchQuery,
                    month = null,
                    medium = null
                ).collectLatest { receipts ->
                    _uiState.value = _uiState.value.copy(receipts = receipts, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    fun toggleFilterDialog() {
        _uiState.value = _uiState.value.copy(
            isFilterDialogOpen = !_uiState.value.isFilterDialogOpen
        )
    }

    private fun observeNetwork() {
        viewModelScope.launch {
            networkMonitor.observeNetworkState().collect { isOnline ->
                val wasOffline = !_uiState.value.isOnline
                _uiState.value = _uiState.value.copy(isOnline = isOnline)

                // If network just came online from offline state, sync
                if (isOnline && wasOffline) {
                    syncFromNetwork()
                }
            }
        }
    }

    private fun syncFromNetwork() {
        viewModelScope.launch {
            Log.d(TAG, "syncFromNetwork: Starting sync process")
            _uiState.value = _uiState.value.copy(isSyncing = true)
            try {
                // First: Push pending receipts to Firestore
                Log.d(TAG, "syncFromNetwork: Pushing pending receipts to Firestore")
                syncPendingReceiptsUseCase().onFailure { error ->
                    Log.e(TAG, "syncFromNetwork: Failed to push pending receipts", error)
                }.onSuccess {
                    Log.d(TAG, "syncFromNetwork: Successfully pushed pending receipts")
                }

                // Second: Get all entries from Firestore (use first() to get single emission)
                Log.d(TAG, "syncFromNetwork: Pulling receipts from Firestore")
                val list = getReceiptsFromFirestoreUseCase().first()
                Log.d(TAG, "syncFromNetwork: Successfully pulled ${list.size} receipts from Firestore")
                Log.d(TAG, "syncFromNetwork: Receipts data: $list")

                list.forEach { receipt ->
                    Log.d(TAG,"syncFromNetwork: Saving receipt - ID: ${receipt.id}, Donor: ${receipt.donorName}")
                    saveReceiptsUseCase(receipt = receipt)
                }
                Log.d(TAG, "syncFromNetwork: All receipts saved to local DB")

            } catch (e: Exception) {
                Log.e(TAG, "syncFromNetwork: Error during sync", e)
                Log.e(TAG, "syncFromNetwork: Exception details: ${e.message}", e)
            } finally {
                Log.d(TAG, "syncFromNetwork: Sync process completed")
                _uiState.value = _uiState.value.copy(isSyncing = false)
            }
        }
    }

    companion object {
        private const val TAG = "ReceiptListViewModel"
    }
}
