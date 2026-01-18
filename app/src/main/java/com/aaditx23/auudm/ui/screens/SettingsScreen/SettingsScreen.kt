package com.aaditx23.auudm.ui.screens.SettingsScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aaditx23.auudm.R
import com.aaditx23.auudm.data.local.datastore.SettingsDataStore
import com.aaditx23.auudm.ui.screens.components.CustomDropdown
import org.koin.compose.koinInject

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val settingsDataStore: SettingsDataStore = koinInject()

    var darkMode by remember { mutableStateOf(settingsDataStore.getDarkMode()) }
    var selectedLanguage by remember { mutableStateOf(if (settingsDataStore.getLanguage() == "en") "English" else "Bangla") }

    val languages = listOf("English", "Bangla")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(R.string.dark_mode))
            Switch(
                checked = darkMode,
                onCheckedChange = {
                    darkMode = it
                    settingsDataStore.setDarkMode(it)
                    (context as? androidx.activity.ComponentActivity)?.recreate()
                }
            )
        }

        CustomDropdown(
            list = languages,
            selected = selectedLanguage,
            onSelect = {
                selectedLanguage = it
                val lang = if (it == "English") "en" else "bn"
                settingsDataStore.setLanguage(lang)
                (context as? androidx.activity.ComponentActivity)?.recreate()
            },
            label = stringResource(R.string.language),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
