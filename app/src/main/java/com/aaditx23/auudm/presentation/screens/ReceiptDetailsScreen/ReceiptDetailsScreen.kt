package com.aaditx23.auudm.presentation.screens.ReceiptDetailsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aaditx23.auudm.R
import com.aaditx23.auudm.presentation.components.AppBarComponent
import com.aaditx23.auudm.presentation.components.DigitalReceipt.DigitalReceiptDialog
import com.aaditx23.auudm.presentation.screens.ReceiptDetailsScreen.components.DeleteConfirmationDialog
import com.aaditx23.auudm.presentation.screens.ReceiptDetailsScreen.components.InfoCard
import com.aaditx23.auudm.presentation.screens.ReceiptDetailsScreen.components.InfoRow
import com.aaditx23.auudm.presentation.util.Constants
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ReceiptDetailsScreen(navController: NavController, receiptId: String) {
    val viewModel: ReceiptDetailsViewModel = koinViewModel { parametersOf(receiptId) }
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val numberFormat = remember { NumberFormat.getInstance(Locale.getDefault()) }
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault())

    val integerFormat = remember { NumberFormat.getIntegerInstance(Locale.getDefault()) }
    integerFormat.isGroupingUsed = false

    val months = Constants.MONTH_IDS.map { stringResource(it) }
    val mediums = Constants.MEDIUM_IDS.map { stringResource(it) }
    val recipients = Constants.RECIPIENT_IDS.map { stringResource(it) }
    val designations = Constants.RECIPIENT_DESIGNATION_IDS.map { stringResource(it) }

    // Get localized error strings
    val deleteNoNetworkMsg = stringResource(R.string.delete_no_network)
    val deleteFailedMsg = stringResource(R.string.delete_failed)

    // Handle delete error
    LaunchedEffect(uiState.deleteError) {
        uiState.deleteError?.let { error ->
            val message = if (error == "delete_no_network") {
                deleteNoNetworkMsg
            } else {
                "$deleteFailedMsg $error"
            }
            snackbarHostState.showSnackbar(message)
            viewModel.clearDeleteError()
        }
    }

    Scaffold(

        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppBarComponent(
                title = stringResource(R.string.receipt_details),
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
        },
        floatingActionButton = {
            if (uiState.receipt != null) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Delete FAB
                    FloatingActionButton(
                        onClick = {
                            if (!uiState.isDeleting) {
                                viewModel.showDeleteConfirmation()
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                    ) {
                        if (uiState.isDeleting) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .height(24.dp)
                                    .width(24.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = stringResource(R.string.delete_receipt)
                            )
                        }
                    }

                    // Preview FAB
                    FloatingActionButton(
                        onClick = { viewModel.showDialog() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = stringResource(R.string.preview_receipt)
                        )
                    }
                }
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
            } else if (uiState.receipt != null) {
                val receiptData = uiState.receipt!!
                val date = dateFormat.format(Date(receiptData.date))

                // Receipt Number Card
                InfoCard(
                    label = stringResource(R.string.receipt_no_label),
                    value = receiptData.id
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
                            label = stringResource(R.string.amount),
                            value = numberFormat.format(receiptData.amount)
                        )

                        InfoRow(
                            label = stringResource(R.string.month),
                            value = receiptData.month.joinToString(", ") { months.getOrNull(it - 1) ?: "Unknown" }
                        )

                        InfoRow(
                            label = stringResource(R.string.year),
                            value = "${integerFormat.format(receiptData.year)}"
                        )

                        InfoRow(
                            label = stringResource(R.string.medium),
                            value = mediums.getOrNull(receiptData.medium - 1) ?: "Unknown"
                        )

                        if (receiptData.mediumReference.isNotBlank()) {
                            InfoRow(
                                label = stringResource(R.string.medium_reference),
                                value = receiptData.mediumReference
                            )
                        }

                        InfoRow(
                            label = stringResource(R.string.date),
                            value = date
                        )

                        InfoRow(
                            label = stringResource(R.string.created_at),
                            value = dateTimeFormat.format(Date(receiptData.createdAt))
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
                            value = recipients.getOrNull(receiptData.recipientIndex) ?: ""
                        )

                        InfoRow(
                            label = stringResource(R.string.recipient_designation),
                            value = designations.getOrNull(receiptData.recipientIndex) ?: ""
                        )
                    }
                }
            }
        }

        if (uiState.showDeleteConfirmation) {
            DeleteConfirmationDialog(
                onConfirm = {
                    viewModel.dismissDeleteConfirmation()
                    viewModel.deleteReceipt {
                        navController.popBackStack()
                    }
                },
                onDismiss = { viewModel.dismissDeleteConfirmation() },
                isDeleting = uiState.isDeleting
            )
        }

        if (uiState.showDialog && uiState.receipt != null) {
            DigitalReceiptDialog(
                receipt = uiState.receipt!!,
                onDismiss = { viewModel.dismissDialog() }
            )
        }
    }


}
