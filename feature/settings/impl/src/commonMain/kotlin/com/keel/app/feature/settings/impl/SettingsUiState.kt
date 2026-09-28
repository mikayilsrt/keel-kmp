package com.keel.app.feature.settings.impl

internal sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val isDarkTheme: Boolean) : SettingsUiState
}
