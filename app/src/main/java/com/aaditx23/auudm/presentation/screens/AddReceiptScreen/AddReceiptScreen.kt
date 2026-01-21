package com.aaditx23.auudm.presentation.screens.AddReceiptScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aaditx23.auudm.R
import com.aaditx23.auudm.domain.model.Receipt
import com.aaditx23.auudm.presentation.components.AppBarComponent
import com.aaditx23.auudm.presentation.components.CustomDropdown
import com.aaditx23.auudm.presentation.components.CustomTextField
import com.aaditx23.auudm.presentation.components.DigitalReceipt.DigitalReceiptDialog
import com.aaditx23.auudm.presentation.util.Constants
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun AddReceiptScreen(navController: NavController) {
    val viewModel: AddReceiptViewModel = koinViewModel()

    val months = Constants.MONTH_IDS.map { stringResource(it) }
    val mediums = Constants.MEDIUM_IDS.map { stringResource(it) }

    val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1

    var donorName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedMonth by remember { mutableStateOf(months[currentMonth - 1]) }
    var amount by remember { mutableStateOf("") }
    var recipientName by remember { mutableStateOf("") }
    var recipientDesignation by remember { mutableStateOf("") }
    var selectedMedium by remember { mutableStateOf(mediums[0]) }
    var mediumReference by remember { mutableStateOf("") }

    // Validation errors
    var donorNameError by remember { mutableStateOf(false) }
    var addressError by remember { mutableStateOf(false) }
    var amountError by remember { mutableStateOf(false) }
    var recipientNameError by remember { mutableStateOf(false) }
    var recipientDesignationError by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }

    // Validation function
    fun validateFields(): Boolean {
        donorNameError = donorName.isBlank()
        addressError = address.isBlank()
        amountError = amount.isBlank() || amount.toDoubleOrNull() == null || amount.toDoubleOrNull()!! <= 0
        recipientNameError = recipientName.isBlank()
        recipientDesignationError = recipientDesignation.isBlank()

        return !donorNameError && !addressError && !amountError && !recipientNameError && !recipientDesignationError
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
                        if (validateFields()) {
                            showDialog = true
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
                        if (validateFields()) {
                            val receipt = Receipt(
                                donorName = donorName,
                                address = address,
                                month = months.indexOf(selectedMonth) + 1,
                                amount = amount.toDoubleOrNull() ?: 0.0,
                                recipientName = recipientName,
                                recipientDesignation = recipientDesignation,
                                medium = mediums.indexOf(selectedMedium) + 1,
                                mediumReference = mediumReference,
                                date = System.currentTimeMillis()
                            )
                            viewModel.saveReceipt(receipt)
                            navController.popBackStack()
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
                value = donorName,
                onValueChange = {
                    donorName = it
                    donorNameError = false
                },
                label = stringResource(R.string.donor_name),
                modifier = Modifier.fillMaxWidth(),
                isError = donorNameError,
                errorMessage = if (donorNameError) "Donor name is required" else null
            )

            CustomTextField(
                value = address,
                onValueChange = {
                    address = it
                    addressError = false
                },
                label = stringResource(R.string.address),
                modifier = Modifier.fillMaxWidth(),
                isError = addressError,
                errorMessage = if (addressError) "Address is required" else null
            )

            CustomDropdown(
                list = months,
                selected = selectedMonth,
                onSelect = { selectedMonth = it },
                label = stringResource(R.string.select_month),
                modifier = Modifier.fillMaxWidth()
            )

            CustomTextField(
                value = amount,
                onValueChange = {
                    amount = it
                    amountError = false
                },
                label = stringResource(R.string.amount),
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Number,
                isError = amountError,
                errorMessage = if (amountError) "Valid amount is required" else null
            )

            CustomTextField(
                value = recipientName,
                onValueChange = {
                    recipientName = it
                    recipientNameError = false
                },
                label = stringResource(R.string.recipient_name),
                modifier = Modifier.fillMaxWidth(),
                isError = recipientNameError,
                errorMessage = if (recipientNameError) "Recipient name is required" else null
            )

            CustomTextField(
                value = recipientDesignation,
                onValueChange = {
                    recipientDesignation = it
                    recipientDesignationError = false
                },
                label = stringResource(R.string.recipient_designation),
                modifier = Modifier.fillMaxWidth(),
                isError = recipientDesignationError,
                errorMessage = if (recipientDesignationError) "Recipient designation is required" else null
            )

            CustomDropdown(
                list = mediums,
                selected = selectedMedium,
                onSelect = { selectedMedium = it },
                label = stringResource(R.string.select_medium),
                modifier = Modifier.fillMaxWidth()
            )

            if (selectedMedium != stringResource(R.string.cash)) {
                CustomTextField(
                    value = mediumReference,
                    onValueChange = { mediumReference = it },
                    label = stringResource(R.string.medium_reference),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    if (showDialog) {
        DigitalReceiptDialog(
            receipt = Receipt(
                id = 0, // Temporary for demo
                donorName = donorName,
                address = address,
                month = months.indexOf(selectedMonth) + 1,
                amount = amount.toDoubleOrNull() ?: 0.0,
                recipientName = recipientName,
                recipientDesignation = recipientDesignation,
                medium = mediums.indexOf(selectedMedium) + 1,
                mediumReference = mediumReference,
                date = System.currentTimeMillis()
            ),
            onDismiss = { showDialog = false }
        )
    }
}
