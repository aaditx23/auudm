package com.aaditx23.auudm.data.local.datastore

import android.content.Context
import android.content.SharedPreferences

class SettingsDataStore(private val context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    companion object {
        private const val DARK_MODE_KEY = "dark_mode"
        private const val LANGUAGE_KEY = "language"
    }

    fun getDarkMode(): Boolean = prefs.getBoolean(DARK_MODE_KEY, false)

    fun getLanguage(): String = prefs.getString(LANGUAGE_KEY, "en") ?: "en"

    fun setDarkMode(enabled: Boolean) {
        prefs.edit().putBoolean(DARK_MODE_KEY, enabled).apply()
    }

    fun setLanguage(language: String) {
        prefs.edit().putString(LANGUAGE_KEY, language).apply()
    }
}
