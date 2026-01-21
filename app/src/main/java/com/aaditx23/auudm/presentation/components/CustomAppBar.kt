package com.aaditx23.auudm.presentation.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.aaditx23.auudm.data.NetworkMonitor
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBarComponent(
    title: String,
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {},
    actions: @Composable () -> Unit = {},
    showNetworkStatus: Boolean = true
) {
    val networkMonitor = koinInject<NetworkMonitor>()
    val isOnline by networkMonitor.observeNetworkState().collectAsState(initial = true)

    TopAppBar(
        windowInsets = WindowInsets(0),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            if (showBackButton) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            actions()
            if (showNetworkStatus) {
                Icon(
                    imageVector = if (isOnline) Icons.Filled.Wifi else Icons.Filled.WifiOff,
                    contentDescription = if (isOnline) "Online" else "Offline",
                    tint = if (isOnline) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}