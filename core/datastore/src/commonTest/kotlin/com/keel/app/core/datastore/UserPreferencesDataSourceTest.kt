package com.keel.app.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import app.cash.turbine.test
import com.keel.app.core.model.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UserPreferencesDataSourceTest {

    private val dataSource = UserPreferencesDataSource(InMemoryPreferencesDataStore())

    @Test
    fun defaultsToLightTheme() = runTest {
        dataSource.userPreferences.test {
            assertEquals(UserPreferences(isDarkTheme = false), awaitItem())
        }
    }

    @Test
    fun persistsDarkTheme() = runTest {
        dataSource.userPreferences.test {
            assertEquals(false, awaitItem().isDarkTheme)
            dataSource.setDarkTheme(true)
            assertEquals(true, awaitItem().isDarkTheme)
        }
    }
}

private class InMemoryPreferencesDataStore : DataStore<Preferences> {
    private val state = MutableStateFlow(emptyPreferences())
    override val data: StateFlow<Preferences> = state

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences =
        transform(state.value).also { state.value = it }
}
