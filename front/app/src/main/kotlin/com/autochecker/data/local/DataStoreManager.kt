package com.autochecker.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.settingsDataStore by preferencesDataStore(name = "settings_prefs")

@Singleton
class DataStoreManager @Inject constructor(
    private val context: Context,
) {
    companion object {
        private val LANGUAGE_KEY = stringPreferencesKey("language")
        private val THEME_KEY = stringPreferencesKey("theme")
        private val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        private val PUSH_REPORT_READY = booleanPreferencesKey("push_report_ready")
        private val PUSH_PROMOTIONS = booleanPreferencesKey("push_promotions")
        private val PUSH_UPDATES = booleanPreferencesKey("push_updates")
    }

    val language: Flow<String> = context.settingsDataStore.data.map { prefs ->
        prefs[LANGUAGE_KEY] ?: "uz"
    }

    suspend fun setLanguage(lang: String) {
        context.settingsDataStore.edit { prefs ->
            prefs[LANGUAGE_KEY] = lang
        }
    }

    val theme: Flow<String> = context.settingsDataStore.data.map { prefs ->
        prefs[THEME_KEY] ?: "dark"
    }

    suspend fun setTheme(theme: String) {
        context.settingsDataStore.edit { prefs ->
            prefs[THEME_KEY] = theme
        }
    }

    val notificationsEnabled: Flow<Boolean> = context.settingsDataStore.data.map { prefs ->
        prefs[NOTIFICATIONS_ENABLED] ?: true
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[NOTIFICATIONS_ENABLED] = enabled
        }
    }

    val pushReportReady: Flow<Boolean> = context.settingsDataStore.data.map { prefs ->
        prefs[PUSH_REPORT_READY] ?: true
    }

    suspend fun setPushReportReady(enabled: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[PUSH_REPORT_READY] = enabled
        }
    }

    val pushPromotions: Flow<Boolean> = context.settingsDataStore.data.map { prefs ->
        prefs[PUSH_PROMOTIONS] ?: false
    }

    suspend fun setPushPromotions(enabled: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[PUSH_PROMOTIONS] = enabled
        }
    }

    val pushUpdates: Flow<Boolean> = context.settingsDataStore.data.map { prefs ->
        prefs[PUSH_UPDATES] ?: true
    }

    suspend fun setPushUpdates(enabled: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[PUSH_UPDATES] = enabled
        }
    }
}
