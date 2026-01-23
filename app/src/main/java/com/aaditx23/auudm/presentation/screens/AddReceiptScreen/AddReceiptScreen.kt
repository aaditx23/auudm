package com.aaditx23.auudm.presentation.screens.AddReceiptScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aaditx23.auudm.R
import com.aaditx23.auudm.presentation.components.AppBarComponent
import com.aaditx23.auudm.presentation.components.CustomDropdown
import com.aaditx23.auudm.presentation.components.CustomTextField
import com.aaditx23.auudm.presentation.components.DigitalReceipt.DigitalReceiptDialog
import com.aaditx23.auudm.presentation.util.Constants
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun AddReceiptScreen(navController: NavController) {
    val viewModel: AddReceiptViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    val months = Constants.MONTH_IDS.map { stringResource(it) }
    val mediums = Constants.MEDIUM_IDS.map { stringResource(it) }
    val recipients = Constants.RECIPIENT_IDS.map { stringResource(it) }
    val designations = Constants.RECIPIENT_DESIGNATION_IDS.map { stringResource(it) }

    val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.updateSelectedMonth(months[currentMonth - 1])
        viewModel.updateSelectedMedium(mediums[0])
        viewModel.updateSelectedRecipient(recipients[0])
    }

    Scaffold(
        topBar = {
            AppBarComponent(
                title = stringResource(R.string.add_receipt),
                showBackButton = true,
                onBackClick = { navController.popBackStack() }
            )
        },
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Preview FAB
                SmallFloatingActionButton(
                    onClick = {
                        if (viewModel.validateFields()) {
                            scope.launch {
                                val receipt = viewModel.saveReceipt(months, mediums, recipients)
                                viewModel.resetForm(months[currentMonth - 1], mediums[0], recipients[0])
                                viewModel.showDialog(receipt, true)

                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Preview Receipt"
                    )
                }

                // Save FAB
                FloatingActionButton(
                    onClick = {
                        if (viewModel.validateFields()) {
                            scope.launch {
                                viewModel.saveReceipt(months, mediums, recipients)
                                navController.popBackStack()
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = stringResource(R.string.save)
                    )
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
            Text(text = stringResource(R.string.date) + ": $currentDate")

            CustomTextField(
                value = uiState.donorName,
                onValueChange = { viewModel.updateDonorName(it) },
                label = stringResource(R.string.donor_name),
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.donorNameError,
                errorMessage = if (uiState.donorNameError) stringResource(R.string.donor_name_required) else null
            )

            CustomTextField(
                value = uiState.address,
                onValueChange = { viewModel.updateAddress(it) },
                label = stringResource(R.string.address),
                modifier = Modifier.fillMaxWidth(),
                isError = uiState.addressError,
                errorMessage = if (uiState.addressError) stringResource(R.string.address_required) else null
            )

            CustomDropdown(
                list = months,
                selected = uiState.selectedMonth,
                onSelect = { viewModel.updateSelectedMonth(it) },
                label = stringResource(R.string.select_month),
                modifier = Modifier.fillMaxWidth()
            )

            CustomTextField(
                value = uiState.amount,
                onValueChange = { viewModel.updateAmount(it) },
                label = stringResource(R.string.amount),
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Number,
                isError = uiState.amountError,
                errorMessage = if (uiState.amountError) stringResource(R.string.amount_required) else null
            )

            CustomDropdown(
                list = recipients,
                selected = uiState.selectedRecipient,
                onSelect = { viewModel.updateSelectedRecipient(it) },
                label = stringResource(R.string.recipient_name),
                modifier = Modifier.fillMaxWidth(),

            )

            // Auto-mapped designation (read-only)
            CustomTextField(
                value = if (uiState.selectedRecipient.isNotBlank() && recipients.contains(uiState.selectedRecipient)) {
                    designations[recipients.indexOf(uiState.selectedRecipient)]
                } else "",
                onValueChange = { },
                label = stringResource(R.string.recipient_designation),
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )

            CustomDropdown(
                list = mediums,
                selected = uiState.selectedMedium,
                onSelect = { viewModel.updateSelectedMedium(it) },
                label = stringResource(R.string.select_medium),
                modifier = Modifier.fillMaxWidth()
            )

            if (uiState.selectedMedium != stringResource(R.string.cash)) {
                CustomTextField(
                    value = uiState.mediumReference,
                    onValueChange = { viewModel.updateMediumReference(it) },
                    label = stringResource(R.string.medium_reference),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    if (uiState.showDialog && uiState.savedReceipt != null) {
        DigitalReceiptDialog(
            receipt = uiState.savedReceipt!!,
            onDismiss = { viewModel.dismissDialog() }
        )
    }
}
