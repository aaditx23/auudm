package com.aaditx23.auudm.data.local.datastore

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsDataStore(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    companion object {
        private const val DARK_MODE_KEY = "dark_mode"
        private const val LANGUAGE_KEY = "language"
    }

    private val _darkModeFlow = MutableStateFlow(getDarkMode())
    val darkModeFlow: StateFlow<Boolean> = _darkModeFlow.asStateFlow()

    private val _languageFlow = MutableStateFlow(getLanguage())
    val languageFlow: StateFlow<String> = _languageFlow.asStateFlow()

    fun getDarkMode(): Boolean = prefs.getBoolean(DARK_MODE_KEY, false)

    fun getLanguage(): String = prefs.getString(LANGUAGE_KEY, "en") ?: "en"

    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean(DARK_MODE_KEY, enabled).apply()
        _darkModeFlow.value = enabled  // Update flow
    }

    fun setLanguage(language: String) {
        prefs.edit().putString(LANGUAGE_KEY, language).apply()
        _languageFlow.value = language  // Update flow
    }
}
