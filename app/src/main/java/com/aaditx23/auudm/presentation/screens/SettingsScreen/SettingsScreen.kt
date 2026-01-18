package com.aaditx23.auudm.presentation.screens.SettingsScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.Language
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
import androidx.navigation.NavController
import com.aaditx23.auudm.R
import com.aaditx23.auudm.data.local.datastore.SettingsDataStore
import com.aaditx23.auudm.presentation.components.AppBarComponent
import org.koin.compose.koinInject

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val settingsDataStore: SettingsDataStore = koinInject()

    var darkMode by remember { mutableStateOf(settingsDataStore.getDarkMode()) }
    var selectedLanguage by remember { mutableStateOf(settingsDataStore.getLanguage()) }

    Scaffold(
        topBar = { AppBarComponent(
            title = stringResource(R.string.settings),
            showBackButton = true,
            onBackClick = { navController.popBackStack() }
        ) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth().height(80.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Filled.Brightness6,
                        contentDescription = null
                    )
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
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clickable {
                        selectedLanguage = if (selectedLanguage == "en") "bn" else "en"
                        settingsDataStore.setLanguage(selectedLanguage)
                        (context as? androidx.activity.ComponentActivity)?.recreate()
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Icon(
                        imageVector = Icons.Filled.Language,
                        contentDescription = null
                    )
                    Text(stringResource(R.string.language))
                    Text(if (selectedLanguage == "en") stringResource(R.string.english) else stringResource(R.string.bangla))
                }
            }
        }
    }
}
