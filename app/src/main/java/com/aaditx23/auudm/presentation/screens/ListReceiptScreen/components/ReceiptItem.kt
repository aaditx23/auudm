package com.aaditx23.auudm.presentation.screens.ListReceiptScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aaditx23.auudm.R
import com.aaditx23.auudm.domain.model.Receipt
import com.aaditx23.auudm.presentation.util.Constants
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReceiptItem(receipt: Receipt) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = dateFormat.format(Date(receipt.date))

    val numberFormat = remember { NumberFormat.getInstance(Locale.getDefault()) }

    val months = Constants.MONTH_IDS.map { stringResource(it) }
    val mediums = Constants.MEDIUM_IDS.map { stringResource(it) }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)
        ) {
            Text("${stringResource(R.string.receipt_no_label)} ${numberFormat.format(receipt.id)}")
            Text("${stringResource(R.string.donor)} ${receipt.donorName}")
            Text("${stringResource(R.string.amount_label)} ${numberFormat.format(receipt.amount)}")
            Text("${stringResource(R.string.date_label)} $date")
            Text("${stringResource(R.string.month_label)} ${months.getOrNull(receipt.month - 1) ?: "Unknown"}")
            Text("${stringResource(R.string.medium_label)} ${mediums.getOrNull(receipt.medium - 1) ?: "Unknown"}")
            if (receipt.mediumReference.isNotBlank()) {
                Text("${stringResource(R.string.reference)} ${receipt.mediumReference}")
            }
        }
    }
}
