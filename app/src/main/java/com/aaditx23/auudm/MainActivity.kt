package com.aaditx23.auudm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.aaditx23.auudm.data.local.datastore.SettingsDataStore
import com.aaditx23.auudm.ui.theme.AUUDMTheme
import com.aaditx23.auudm.presentation.util.LocaleWrapper
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val settingsDataStore: SettingsDataStore by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            // Observe dark mode from DataStore Flow - reactive!
            val darkMode by settingsDataStore.darkModeFlow.collectAsState(initial = settingsDataStore.getDarkMode())

            // Observe language from DataStore Flow - reactive!
            val language by settingsDataStore.languageFlow.collectAsState(initial = settingsDataStore.getLanguage())

            LocaleWrapper(language = language) {
                AUUDMTheme(
                    darkTheme = darkMode
                ) {
                    App()
                }
            }
        }
    }
}