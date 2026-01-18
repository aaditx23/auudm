package com.aaditx23.auudm.presentation.screens.ListReceiptScreen.components

import androidx.compose.foundation.clickable
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
import androidx.navigation.NavController
import com.aaditx23.auudm.R
import com.aaditx23.auudm.domain.model.Receipt
import com.aaditx23.auudm.presentation.util.Constants
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ReceiptItem(receipt: Receipt, navController: NavController) {
    val numberFormat = remember { NumberFormat.getInstance(Locale.getDefault()) }

    val months = Constants.MONTH_IDS.map { stringResource(it) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navController.navigate("receipt_details/${receipt.id}") }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp)
        ) {
            Text("${stringResource(R.string.receipt_no_label)} ${numberFormat.format(receipt.id)}")
            Text("${stringResource(R.string.donor)} ${receipt.donorName}")
            Text("${stringResource(R.string.month_label)} ${months.getOrNull(receipt.month - 1) ?: "Unknown"}")
        }
    }
}
