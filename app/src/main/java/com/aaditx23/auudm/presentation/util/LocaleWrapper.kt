package com.aaditx23.auudm.presentation.util

import android.content.Context
import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

@Composable
fun LocaleWrapper(
    language: String,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val localeContext = remember(language) {
        updateLocale(context, language)
    }

    CompositionLocalProvider(
        LocalContext provides localeContext
    ) {
        content()
    }
}

private fun updateLocale(context: Context, language: String): Context {
    val locale = Locale.forLanguageTag(language)
    Locale.setDefault(locale)

    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)

    return context.createConfigurationContext(config)
}

