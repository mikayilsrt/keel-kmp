package com.keel.app.core.testing.repository

import com.keel.app.core.data.repository.UserPreferencesRepository
import com.keel.app.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserPreferencesRepository(
    initial: UserPreferences = UserPreferences(),
) : UserPreferencesRepository {
    private val preferences = MutableStateFlow(initial)

    override val userPreferences: Flow<UserPreferences> = preferences

    override suspend fun setDarkTheme(enabled: Boolean) {
        preferences.value = preferences.value.copy(isDarkTheme = enabled)
    }
}
