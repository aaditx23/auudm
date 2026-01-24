package com.aaditx23.auudm.presentation.screens.ListReceiptScreen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aaditx23.auudm.R
import com.aaditx23.auudm.presentation.components.AppBarComponent
import com.aaditx23.auudm.presentation.components.CustomTextField
import com.aaditx23.auudm.presentation.screens.ListReceiptScreen.components.FilterDialog
import com.aaditx23.auudm.presentation.screens.ListReceiptScreen.components.ReceiptItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReceiptListScreen(navController: NavController) {
    val viewModel: ReceiptListViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            AppBarComponent(
                title = stringResource(R.string.receipts_list),
                actions = {
                    if (uiState.isSyncing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    else{
                        Icon(
                            imageVector = if (uiState.isOnline) Icons.Filled.Wifi else Icons.Filled.WifiOff,
                            contentDescription = if (uiState.isOnline) "Online" else "Offline",
                            tint = if (uiState.isOnline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                        )
                    }

                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add_receipt") }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Receipt")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.searchReceipts(it) },
                    label = stringResource(R.string.search),
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { viewModel.toggleFilterDialog() }
                ) {
                    Icon(
                        imageVector = Icons.Filled.FilterList,
                        contentDescription = stringResource(R.string.filter),
                        tint = if (uiState.filterMonth != null || uiState.filterYear != null || uiState.filterMedium != null)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (uiState.isLoading) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.error != null) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = uiState.error ?: "Unknown error")
                }
            } else if (uiState.receipts.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.no_receipts),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.receipts) { receipt ->
                        ReceiptItem(receipt, navController)
                    }
                }
            }
        }

        // Filter Dialog
        if (uiState.isFilterDialogOpen) {
            FilterDialog(
                currentMonth = uiState.filterMonth,
                currentYear = uiState.filterYear,
                currentMedium = uiState.filterMedium,
                availableYears = uiState.availableYears,
                onApplyFilter = { month, year, medium ->
                    viewModel.applyFilters(month, year, medium)
                },
                onClearFilter = {
                    viewModel.clearFilters()
                },
                onDismiss = {
                    viewModel.toggleFilterDialog()
                }
            )
        }
    }
}
