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
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterDialog(
    currentMonth: Int?,
    currentYear: Int?,
    currentMedium: Int?,
    availableYears: List<Int>,
    onApplyFilter: (month: Int?, year: Int?, medium: Int?) -> Unit,
    onClearFilter: () -> Unit,
    onDismiss: () -> Unit
) {
    // Load string resources
    val filterReceiptsText = stringResource(R.string.filter_receipts)
    val allText = stringResource(R.string.all)
    val monthText = stringResource(R.string.month)
    val yearText = stringResource(R.string.year)
    val mediumText = stringResource(R.string.medium)
    val closeText = stringResource(R.string.close)
    val clearFilterText = stringResource(R.string.clear_filter)
    val applyFilterText = stringResource(R.string.apply_filter)

    val integerFormat = remember { NumberFormat.getIntegerInstance(Locale.getDefault()) }
    integerFormat.isGroupingUsed = false

    val years = listOf(stringResource(R.string.all)) + availableYears.map { integerFormat.format(it) }

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
    var selectedYear by remember {
        val selectedYearIndex = if (currentYear == null) 0 else availableYears.indexOf(currentYear) + 1
        mutableStateOf(years[selectedYearIndex])
    }
    var selectedMedium by remember { mutableStateOf(mediums[currentMedium ?: 0]) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = filterReceiptsText,
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
                    label = monthText,
                    modifier = Modifier.fillMaxWidth()
                )

                // Year Filter
                CustomDropdown(
                    list = years,
                    selected = selectedYear,
                    onSelect = { selectedYear = it },
                    label = yearText,
                    modifier = Modifier.fillMaxWidth()
                )

                // Medium Filter
                CustomDropdown(
                    list = mediums,
                    selected = selectedMedium,
                    onSelect = { selectedMedium = it },
                    label = mediumText,
                    modifier = Modifier.fillMaxWidth()
                )

            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text(closeText)
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
                    Text(clearFilterText)
                }

                Button(
                    onClick = {
                        val monthIndex = months.indexOf(selectedMonth)
                        val yearIndex = years.indexOf(selectedYear)
                        val mediumIndex = mediums.indexOf(selectedMedium)
                        val month = if (monthIndex == 0) null else monthIndex
                        val year = if (yearIndex == 0) null else availableYears[yearIndex - 1]
                        val medium = if (mediumIndex == 0) null else mediumIndex
                        onApplyFilter(month, year, medium)
                        onDismiss()
                    },
                ) {
                    Text(applyFilterText)
                }
            }
        }
    )
}
