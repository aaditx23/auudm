package com.aaditx23.auudm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aaditx23.auudm.data.local.datastore.SettingsDataStore
import com.aaditx23.auudm.ui.navigation.Screen
import com.aaditx23.auudm.ui.screens.AddReceiptScreen.AddReceiptScreen
import com.aaditx23.auudm.ui.screens.ListReceiptScreen.ListReceiptScreen
import com.aaditx23.auudm.ui.screens.SettingsScreen.SettingsScreen
import com.aaditx23.auudm.ui.theme.AUUDMTheme
import com.aaditx23.auudm.ui.util.LocaleManager

class MainActivity : ComponentActivity() {

    private lateinit var settingsDataStore: SettingsDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settingsDataStore = SettingsDataStore(this)

        // Apply locale
        val language = settingsDataStore.getLanguage()
        val context = LocaleManager.setLocale(this, language)
        resources.updateConfiguration(context.resources.configuration, context.resources.displayMetrics)

        enableEdgeToEdge()
        setContent {
            AUUDMTheme(
                darkTheme = settingsDataStore.getDarkMode()
            ) {
                val navController = rememberNavController()

                val items = listOf(
                    Screen.AddReceipt,
                    Screen.ListReceipts,
                    Screen.Settings
                )

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
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
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.AddReceipt.route) { AddReceiptScreen() }
                        composable(Screen.ListReceipts.route) { ListReceiptScreen() }
                        composable(Screen.Settings.route) { SettingsScreen() }
                    }
                }
            }
        }
    }
}