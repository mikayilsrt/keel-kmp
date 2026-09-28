package com.keel.app.feature.settings.impl

import app.cash.turbine.test
import com.keel.app.core.model.UserPreferences
import com.keel.app.core.testing.repository.FakeUserPreferencesRepository
import com.keel.app.core.testing.runMainTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SettingsViewModelTest {

    @Test
    fun uiState_reflectsStoredPreference() = runMainTest {
        val viewModel = SettingsViewModel(FakeUserPreferencesRepository(UserPreferences(isDarkTheme = true)))
        viewModel.uiState.test {
            assertEquals(SettingsUiState.Success(isDarkTheme = true), awaitItem())
        }
    }

    @Test
    fun setDarkTheme_updatesUiState() = runMainTest {
        val viewModel = SettingsViewModel(FakeUserPreferencesRepository())
        viewModel.uiState.test {
            assertEquals(SettingsUiState.Success(isDarkTheme = false), awaitItem())
            viewModel.setDarkTheme(true)
            assertEquals(SettingsUiState.Success(isDarkTheme = true), awaitItem())
        }
    }
}
