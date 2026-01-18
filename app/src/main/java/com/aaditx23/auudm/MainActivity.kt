package com.aaditx23.auudm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.aaditx23.auudm.data.local.datastore.SettingsDataStore
import com.aaditx23.auudm.ui.theme.AUUDMTheme
import com.aaditx23.auudm.presentation.util.LocaleManager

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
                App()
            }
        }
    }
}