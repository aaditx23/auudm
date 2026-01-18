package com.aaditx23.auudm.presentation.screens.ReceiptDetailsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aaditx23.auudm.R
import com.aaditx23.auudm.presentation.components.AppBarComponent
import com.aaditx23.auudm.presentation.util.Constants
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ReceiptDetailsScreen(navController: NavController, receiptId: Long) {
    val viewModel: ReceiptDetailsViewModel = koinViewModel { parametersOf(receiptId) }
    val receipt by viewModel.receipt.collectAsState()

    val numberFormat = remember { NumberFormat.getInstance(Locale.getDefault()) }
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    val months = Constants.MONTH_IDS.map { stringResource(it) }
    val mediums = Constants.MEDIUM_IDS.map { stringResource(it) }

    receipt?.let {
        val date = dateFormat.format(Date(it.date))

        Scaffold(
            topBar = { AppBarComponent(
                title = stringResource(R.string.receipt_details),
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            ) }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("${stringResource(R.string.receipt_no_label)} ${numberFormat.format(it.id)}")
                Text("${stringResource(R.string.donor)} ${it.donorName}")
                Text("${stringResource(R.string.address)} ${it.address}")
                Text("${stringResource(R.string.month_label)} ${months.getOrNull(it.month - 1) ?: "Unknown"}")
                Text("${stringResource(R.string.amount_label)} ${numberFormat.format(it.amount)}")
                Text("${stringResource(R.string.recipient_name)} ${it.recipientName}")
                Text("${stringResource(R.string.recipient_designation)} ${it.recipientDesignation}")
                Text("${stringResource(R.string.medium_label)} ${mediums.getOrNull(it.medium - 1) ?: "Unknown"}")
                Text("${stringResource(R.string.medium_reference)} ${it.mediumReference}")
                Text("${stringResource(R.string.date_label)} $date")
            }
        }
    }
}
