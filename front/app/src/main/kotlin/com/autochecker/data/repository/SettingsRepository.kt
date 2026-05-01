package com.autochecker.data.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val language: Flow<String>
    val theme: Flow<String>
    val notificationsEnabled: Flow<Boolean>
    val pushReportReady: Flow<Boolean>
    val pushPromotions: Flow<Boolean>
    val pushUpdates: Flow<Boolean>

    suspend fun setLanguage(lang: String)
    suspend fun setTheme(theme: String)
    suspend fun setNotificationsEnabled(enabled: Boolean)
    suspend fun setPushReportReady(enabled: Boolean)
    suspend fun setPushPromotions(enabled: Boolean)
    suspend fun setPushUpdates(enabled: Boolean)
}
