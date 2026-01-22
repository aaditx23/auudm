package com.aaditx23.auudm

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aaditx23.auudm.presentation.navigation.Screen
import com.aaditx23.auudm.presentation.screens.AddReceiptScreen.AddReceiptScreen
import com.aaditx23.auudm.presentation.screens.ListReceiptScreen.ReceiptListScreen
import com.aaditx23.auudm.presentation.screens.SettingsScreen.SettingsScreen
import com.aaditx23.auudm.presentation.screens.ReceiptDetailsScreen.ReceiptDetailsScreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun App() {
    val navController = rememberNavController()

    val items = listOf(
        Screen.ListReceipts,
        Screen.Settings
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            NavigationBar {
                val navBackStackEntry = navController.currentBackStackEntryAsState().value
                val currentDestination = navBackStackEntry?.destination
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = null) },
                        label = { Text(stringResource(screen.labelRes)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.ListReceipts.route,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable(Screen.AddReceipt.route) { AddReceiptScreen(navController) }
            composable(Screen.ListReceipts.route) { ReceiptListScreen(navController) }
            composable(Screen.Settings.route) { SettingsScreen(navController) }
            composable("receipt_details/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id") ?: ""
                ReceiptDetailsScreen(navController, id)
            }
        }
    }
}
