package com.keel.app.shell

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keel.app.core.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class AppViewModel(
    userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    val uiState: StateFlow<AppUiState> = userPreferencesRepository.userPreferences
        .map { AppUiState.Success(isDarkTheme = it.isDarkTheme) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppUiState.Loading,
        )
}

internal sealed interface AppUiState {
    data object Loading : AppUiState
    data class Success(val isDarkTheme: Boolean) : AppUiState
}
