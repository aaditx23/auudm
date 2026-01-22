package com.aaditx23.auudm.presentation.screens.SettingsScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness6
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Language
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val settingsDataStore: SettingsDataStore = koinInject()

    var darkMode by remember { mutableStateOf(settingsDataStore.getDarkMode()) }
    var selectedLanguage by remember { mutableStateOf(settingsDataStore.getLanguage()) }

    val syncState by viewModel.syncState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val successMessage = stringResource(R.string.sync_all_success)
    val errorMessage = stringResource(R.string.sync_failed) + " ${syncState.syncError}"

    // Show snackbar when sync completes
    LaunchedEffect(syncState) {
        if (syncState.syncSuccess) {
            scope.launch {
                snackbarHostState.showSnackbar(successMessage)
                viewModel.clearSyncState()
            }
        } else if (syncState.syncError != null) {
            scope.launch {
                snackbarHostState.showSnackbar(errorMessage)
                viewModel.clearSyncState()
            }
        }
    }

    Scaffold(
        topBar = { AppBarComponent(
            title = stringResource(R.string.settings),
            showBackButton = false,
        ) },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Dark Mode Setting
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
                            // No recreate() needed! Theme updates automatically via Flow
                        }
                    )
                }
            }

            // Language Setting
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                onClick = {
                    selectedLanguage = if (selectedLanguage == "en") "bn" else "en"
                    settingsDataStore.setLanguage(selectedLanguage)
                    // No recreate() needed! Locale updates automatically via Flow
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

            // Cloud Backup Section
            Text(
                text = stringResource(R.string.cloud_backup),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CloudUpload,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = stringResource(R.string.firestore_backup),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }

                    Text(
                        text = stringResource(R.string.sync_all_description),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Button(
                        onClick = { viewModel.syncAll() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !syncState.isSyncing
                    ) {
                        if (syncState.isSyncing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.syncing))
                        } else {
                            Icon(
                                imageVector = Icons.Filled.CloudUpload,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.sync_all_receipts))
                        }
                    }
                }
            }
        }
    }
}
