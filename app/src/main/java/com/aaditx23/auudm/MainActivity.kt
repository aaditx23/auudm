package com.aaditx23.auudm

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.aaditx23.auudm.data.local.datastore.SettingsDataStore
import com.aaditx23.auudm.ui.theme.AUUDMTheme
import com.aaditx23.auudm.presentation.util.LocaleManager
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val settingsDataStore: SettingsDataStore by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            // Observe dark mode from DataStore Flow - reactive!
            val darkMode by settingsDataStore.darkModeFlow.collectAsState(initial = settingsDataStore.getDarkMode())

            // Observe language from DataStore Flow - reactive!
            val language by settingsDataStore.languageFlow.collectAsState(initial = settingsDataStore.getLanguage())

            // Create localized context when language changes
            val localizedContext = remember(language) {
                LocaleManager.setLocale(this@MainActivity, language)
            }

            // Update system bars when dark mode changes
            LaunchedEffect(darkMode) {
                enableEdgeToEdge(
                    statusBarStyle = if (darkMode) {
                        SystemBarStyle.dark(
                            Color.TRANSPARENT
                        )
                    } else {
                        SystemBarStyle.light(
                            Color.TRANSPARENT,
                            Color.TRANSPARENT
                        )
                    },
                    navigationBarStyle = if (darkMode) {
                        SystemBarStyle.dark(
                            Color.TRANSPARENT
                        )
                    } else {
                        SystemBarStyle.light(
                            Color.TRANSPARENT,
                            Color.TRANSPARENT
                        )
                    }
                )
            }

            // Provide the localized context to all composables
            CompositionLocalProvider(LocalContext provides localizedContext) {
                AUUDMTheme(
                    darkTheme = darkMode
                ) {
                    App()
                }
            }
        }
    }
}
