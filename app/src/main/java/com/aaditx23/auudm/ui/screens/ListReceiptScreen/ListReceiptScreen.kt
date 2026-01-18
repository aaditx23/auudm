package com.aaditx23.auudm.ui.screens.ListReceiptScreen

import CustomTextField
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aaditx23.auudm.R
import com.aaditx23.auudm.ui.screens.ListReceiptScreen.components.ReceiptItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun ListReceiptScreen() {
    val viewModel: ListReceiptViewModel = koinViewModel()
    val receipts by viewModel.receipts.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CustomTextField(
            value = searchQuery,
            onValueChange = { viewModel.searchReceipts(it) },
            label = stringResource(R.string.search),
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(receipts) { receipt ->
                ReceiptItem(receipt)
            }
        }

        if (receipts.isEmpty()) {
            Text(stringResource(R.string.no_receipts))
        }
    }
}
