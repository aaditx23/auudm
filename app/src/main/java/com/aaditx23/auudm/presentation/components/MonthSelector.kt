package com.aaditx23.auudm.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aaditx23.auudm.R
import com.aaditx23.auudm.presentation.util.Constants

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MonthSelector(
    selectedMonths: List<Int>,
    onMonthToggle: (Int) -> Unit,
    onSelectAll: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val monthNames = Constants.MONTH_IDS.map { stringResource(it) }
    val allSelected = selectedMonths.size == 12

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.select_month),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Select All Checkbox
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Checkbox(
                checked = allSelected,
                onCheckedChange = { checked ->
                    onSelectAll(checked)
                }
            )
            Text(
                text = stringResource(R.string.select_all),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            maxItemsInEachRow = 6
        ) {
            monthNames.forEachIndexed { index, monthName ->
                val month = index + 1
                val isSelected = selectedMonths.contains(month)
                Card(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(48.dp),
                    onClick = {
                        onMonthToggle(month)
                    },
                    shape = CircleShape,
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = if (isSelected) 0.dp else 3.dp
                    )
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = monthName,
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
