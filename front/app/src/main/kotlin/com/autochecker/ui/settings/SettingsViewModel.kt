package com.autochecker.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autochecker.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    val language: StateFlow<String> = settingsRepository.language
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "uz")

    val theme: StateFlow<String> = settingsRepository.theme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "dark")

    val notificationsEnabled: StateFlow<Boolean> = settingsRepository.notificationsEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val pushReportReady: StateFlow<Boolean> = settingsRepository.pushReportReady
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val pushPromotions: StateFlow<Boolean> = settingsRepository.pushPromotions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val pushUpdates: StateFlow<Boolean> = settingsRepository.pushUpdates
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun setLanguage(lang: String) {
        viewModelScope.launch {
            settingsRepository.setLanguage(lang)
            val localeList = LocaleListCompat.forLanguageTags(lang)
            AppCompatDelegate.setApplicationLocales(localeList)
        }
    }

    fun setTheme(theme: String) {
        viewModelScope.launch {
            settingsRepository.setTheme(theme)
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setNotificationsEnabled(enabled)
        }
    }

    fun setPushReportReady(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setPushReportReady(enabled)
        }
    }

    fun setPushPromotions(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setPushPromotions(enabled)
        }
    }

    fun setPushUpdates(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setPushUpdates(enabled)
        }
    }
}
