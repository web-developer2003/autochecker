package com.autochecker.data.mock

import com.autochecker.data.local.DataStoreManager
import com.autochecker.data.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MockSettingsRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager,
) : SettingsRepository {
    override val language: Flow<String> = dataStoreManager.language
    override val theme: Flow<String> = dataStoreManager.theme
    override val notificationsEnabled: Flow<Boolean> = dataStoreManager.notificationsEnabled
    override val pushReportReady: Flow<Boolean> = dataStoreManager.pushReportReady
    override val pushPromotions: Flow<Boolean> = dataStoreManager.pushPromotions
    override val pushUpdates: Flow<Boolean> = dataStoreManager.pushUpdates

    override suspend fun setLanguage(lang: String) = dataStoreManager.setLanguage(lang)
    override suspend fun setTheme(theme: String) = dataStoreManager.setTheme(theme)
    override suspend fun setNotificationsEnabled(enabled: Boolean) = dataStoreManager.setNotificationsEnabled(enabled)
    override suspend fun setPushReportReady(enabled: Boolean) = dataStoreManager.setPushReportReady(enabled)
    override suspend fun setPushPromotions(enabled: Boolean) = dataStoreManager.setPushPromotions(enabled)
    override suspend fun setPushUpdates(enabled: Boolean) = dataStoreManager.setPushUpdates(enabled)
}
