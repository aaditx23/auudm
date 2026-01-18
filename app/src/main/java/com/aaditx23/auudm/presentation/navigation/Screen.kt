package com.aaditx23.auudm.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.aaditx23.auudm.R

sealed class Screen(val route: String, val icon: ImageVector, val labelRes: Int) {
    object AddReceipt : Screen("add_receipt", Icons.Default.Add, R.string.add_receipt)
    object ListReceipts : Screen("list_receipts", Icons.AutoMirrored.Filled.List, R.string.list_receipts)
    object Settings : Screen("settings", Icons.Default.Settings, R.string.settings)
}
