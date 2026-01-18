package com.aaditx23.auudm.ui.screens.ListReceiptScreen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aaditx23.auudm.domain.model.Receipt
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReceiptItem(receipt: Receipt) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = dateFormat.format(Date(receipt.date))

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)
        ) {
            Text("Receipt No: ${receipt.receiptNo}")
            Text("Donor: ${receipt.donorName}")
            Text("Amount: ${receipt.amount}")
            Text("Date: $date")
            Text("Month: ${receipt.month}")
            Text("Medium: ${receipt.medium}")
            if (receipt.mediumReference.isNotBlank()) {
                Text("Reference: ${receipt.mediumReference}")
            }
        }
    }
}
