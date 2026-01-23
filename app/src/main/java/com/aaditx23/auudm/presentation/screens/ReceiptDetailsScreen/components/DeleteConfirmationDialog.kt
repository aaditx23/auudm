package com.aaditx23.auudm.presentation.screens.ReceiptDetailsScreen.components

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.aaditx23.auudm.R
import com.aaditx23.auudm.data.local.datastore.SettingsDataStore
import org.koin.compose.koinInject
import java.util.Locale

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    isDeleting: Boolean = false
) {
    val context = LocalContext.current
    val settingsDataStore: SettingsDataStore = koinInject()
    val language by settingsDataStore.languageFlow.collectAsState(initial = settingsDataStore.getLanguage())

    // Create localized resources based on current language
    val localizedResources = remember(language) {
        val locale = Locale.forLanguageTag(language)
        val configuration = Configuration()
        configuration.setLocale(locale)
        context.createConfigurationContext(configuration).resources
    }

    // Load localized strings
    val confirmDeleteText = localizedResources.getString(R.string.confirm_delete)
    val deleteConfirmationText = localizedResources.getString(R.string.delete_confirmation)
    val deleteText = localizedResources.getString(R.string.delete)
    val cancelText = localizedResources.getString(R.string.cancel)

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = confirmDeleteText) },
        text = { Text(deleteConfirmationText) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isDeleting,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
            ) {
                Text(deleteText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isDeleting
            ) {
                Text(cancelText)
            }
        }
    )
}

