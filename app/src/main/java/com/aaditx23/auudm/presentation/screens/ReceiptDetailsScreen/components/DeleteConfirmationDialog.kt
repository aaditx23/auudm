package com.aaditx23.auudm.presentation.screens.ReceiptDetailsScreen.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.aaditx23.auudm.R

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isDeleting: Boolean = false
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.confirm_delete)) },
        text = { Text(stringResource(R.string.delete_confirmation)) },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = !isDeleting
            ) {
                Text(stringResource(R.string.delete))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isDeleting
            ) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

