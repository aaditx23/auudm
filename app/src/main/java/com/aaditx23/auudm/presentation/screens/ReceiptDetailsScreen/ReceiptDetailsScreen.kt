package com.aaditx23.auudm.presentation.screens.ReceiptDetailsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aaditx23.auudm.R
import com.aaditx23.auudm.presentation.components.AppBarComponent
import com.aaditx23.auudm.presentation.components.DigitalReceipt.DigitalReceiptDialog
import com.aaditx23.auudm.presentation.screens.ReceiptDetailsScreen.components.InfoCard
import com.aaditx23.auudm.presentation.screens.ReceiptDetailsScreen.components.InfoRow
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

    var showDialog by remember { mutableStateOf(false) }

    receipt?.let { receiptData ->
        val date = dateFormat.format(Date(receiptData.date))

        Scaffold(
            topBar = {
                AppBarComponent(
                    title = stringResource(R.string.receipt_details),
                    showBackButton = true,
                    onBackClick = { navController.popBackStack() }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showDialog = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Preview Receipt"
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Receipt Number Card
                InfoCard(
                    label = stringResource(R.string.receipt_no_label),
                    value = numberFormat.format(receiptData.id)
                )

                // Donor Information Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.donor_information),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        InfoRow(
                            label = stringResource(R.string.donor),
                            value = receiptData.donorName
                        )

                        InfoRow(
                            label = stringResource(R.string.address),
                            value = receiptData.address
                        )
                    }
                }

                // Payment Details Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.payment_details),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        InfoRow(
                            label = stringResource(R.string.amount_label),
                            value = numberFormat.format(receiptData.amount)
                        )

                        InfoRow(
                            label = stringResource(R.string.month_label),
                            value = months.getOrNull(receiptData.month - 1) ?: "Unknown"
                        )

                        InfoRow(
                            label = stringResource(R.string.medium_label),
                            value = mediums.getOrNull(receiptData.medium - 1) ?: "Unknown"
                        )

                        if (receiptData.mediumReference.isNotBlank()) {
                            InfoRow(
                                label = stringResource(R.string.medium_reference),
                                value = receiptData.mediumReference
                            )
                        }

                        InfoRow(
                            label = stringResource(R.string.date_label),
                            value = date
                        )
                    }
                }

                // Recipient Information Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.recipient_information),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        InfoRow(
                            label = stringResource(R.string.recipient_name),
                            value = receiptData.recipientName
                        )

                        InfoRow(
                            label = stringResource(R.string.recipient_designation),
                            value = receiptData.recipientDesignation
                        )
                    }
                }
            }
        }

        if (showDialog) {
            DigitalReceiptDialog(
                receipt = receiptData,
                onDismiss = { showDialog = false }
            )
        }
    }
}

