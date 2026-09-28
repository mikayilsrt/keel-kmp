package com.keel.app.shell

import app.cash.turbine.test
import com.keel.app.core.testing.repository.FakeUserPreferencesRepository
import com.keel.app.core.testing.runMainTest
import kotlin.test.Test
import kotlin.test.assertEquals

class AppViewModelTest {

    @Test
    fun uiState_followsDarkThemePreference() = runMainTest {
        val repository = FakeUserPreferencesRepository()
        val viewModel = AppViewModel(repository)
        viewModel.uiState.test {
            assertEquals(AppUiState.Success(isDarkTheme = false), awaitItem())
            repository.setDarkTheme(true)
            assertEquals(AppUiState.Success(isDarkTheme = true), awaitItem())
        }
    }
}
