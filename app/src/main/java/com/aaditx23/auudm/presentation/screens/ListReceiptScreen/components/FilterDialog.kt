package com.aaditx23.auudm.presentation.screens.ListReceiptScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.aaditx23.auudm.R
import com.aaditx23.auudm.presentation.components.CustomDropdown

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    currentMonth: Int?,
    currentMedium: Int?,
    onApplyFilter: (month: Int?, medium: Int?) -> Unit,
    onClearFilter: () -> Unit,
    onDismiss: () -> Unit
) {
    val months = listOf(
        stringResource(R.string.all),
        stringResource(R.string.january),
        stringResource(R.string.february),
        stringResource(R.string.march),
        stringResource(R.string.april),
        stringResource(R.string.may),
        stringResource(R.string.june),
        stringResource(R.string.july),
        stringResource(R.string.august),
        stringResource(R.string.september),
        stringResource(R.string.october),
        stringResource(R.string.november),
        stringResource(R.string.december)
    )

    val mediums = listOf(
        stringResource(R.string.all),
        stringResource(R.string.bkash),
        stringResource(R.string.bank),
        stringResource(R.string.cash)
    )

    var selectedMonth by remember { mutableStateOf(months[currentMonth ?: 0]) }
    var selectedMedium by remember { mutableStateOf(mediums[currentMedium ?: 0]) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.filter_receipts),
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {


                Spacer(modifier = Modifier.height(8.dp))

                // Month Filter
                CustomDropdown(
                    list = months,
                    selected = selectedMonth,
                    onSelect = { selectedMonth = it },
                    label = stringResource(R.string.month),
                    modifier = Modifier.fillMaxWidth()
                )

                // Medium Filter
                CustomDropdown(
                    list = mediums,
                    selected = selectedMedium,
                    onSelect = { selectedMedium = it },
                    label = stringResource(R.string.medium),
                    modifier = Modifier.fillMaxWidth()
                )

            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text(stringResource(R.string.close))
            }
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedButton(
                    onClick = {
                        onClearFilter()
                        onDismiss()
                    },
                ) {
                    Text(stringResource(R.string.clear_filter))
                }

                Button(
                    onClick = {
                        val monthIndex = months.indexOf(selectedMonth)
                        val mediumIndex = mediums.indexOf(selectedMedium)
                        val month = if (monthIndex == 0) null else monthIndex
                        val medium = if (mediumIndex == 0) null else mediumIndex
                        onApplyFilter(month, medium)
                        onDismiss()
                    },
                ) {
                    Text(stringResource(R.string.apply_filter))
                }
            }
        }
    )
}

