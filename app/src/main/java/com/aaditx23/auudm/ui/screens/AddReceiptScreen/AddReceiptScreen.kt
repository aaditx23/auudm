package com.aaditx23.auudm.ui.screens.AddReceiptScreen

import CustomTextField
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aaditx23.auudm.R
import com.aaditx23.auudm.domain.model.Receipt
import com.aaditx23.auudm.ui.screens.components.CustomDropdown
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AddReceiptScreen(
    onSave: () -> Unit = {}
) {
    val viewModel: AddReceiptViewModel = koinViewModel()
    val context = LocalContext.current

    var receiptNo by remember { mutableStateOf("") }
    var donorName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var selectedMonth by remember { mutableStateOf<String?>(null) }
    var amount by remember { mutableStateOf("") }
    var recipientName by remember { mutableStateOf("") }
    var recipientDesignation by remember { mutableStateOf("") }
    var selectedMedium by remember { mutableStateOf<String?>(null) }
    var mediumReference by remember { mutableStateOf("") }

    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )
    val mediums = listOf("bKash", "Bank", "Cash")

    val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = stringResource(R.string.date) + ": $currentDate")

        CustomTextField(
            value = receiptNo,
            onValueChange = { receiptNo = it },
            label = stringResource(R.string.receipt_no),
            modifier = Modifier.fillMaxWidth()
        )

        CustomTextField(
            value = donorName,
            onValueChange = { donorName = it },
            label = stringResource(R.string.donor_name),
            modifier = Modifier.fillMaxWidth()
        )

        CustomTextField(
            value = address,
            onValueChange = { address = it },
            label = stringResource(R.string.address),
            modifier = Modifier.fillMaxWidth()
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
            onValueChange = { amount = it },
            label = stringResource(R.string.amount),
            modifier = Modifier.fillMaxWidth()
        )

        CustomTextField(
            value = recipientName,
            onValueChange = { recipientName = it },
            label = stringResource(R.string.recipient_name),
            modifier = Modifier.fillMaxWidth()
        )

        CustomTextField(
            value = recipientDesignation,
            onValueChange = { recipientDesignation = it },
            label = stringResource(R.string.recipient_designation),
            modifier = Modifier.fillMaxWidth()
        )

        CustomDropdown(
            list = mediums,
            selected = selectedMedium,
            onSelect = { selectedMedium = it },
            label = stringResource(R.string.select_medium),
            modifier = Modifier.fillMaxWidth()
        )

        if (selectedMedium != "Cash") {
            CustomTextField(
                value = mediumReference,
                onValueChange = { mediumReference = it },
                label = stringResource(R.string.medium_reference),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = {
                val receipt = Receipt(
                    receiptNo = receiptNo,
                    donorName = donorName,
                    address = address,
                    month = selectedMonth ?: "",
                    amount = amount.toDoubleOrNull() ?: 0.0,
                    recipientName = recipientName,
                    recipientDesignation = recipientDesignation,
                    medium = selectedMedium ?: "",
                    mediumReference = mediumReference,
                    date = System.currentTimeMillis()
                )
                viewModel.saveReceipt(receipt)
                onSave()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save))
        }
    }
}
