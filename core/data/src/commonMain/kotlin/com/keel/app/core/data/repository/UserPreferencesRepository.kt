package com.keel.app.core.data.repository

import com.keel.app.core.datastore.UserPreferencesDataSource
import com.keel.app.core.model.UserPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userPreferences: Flow<UserPreferences>
    suspend fun setDarkTheme(enabled: Boolean)
}

internal class DefaultUserPreferencesRepository(
    private val dataSource: UserPreferencesDataSource,
) : UserPreferencesRepository {
    override val userPreferences: Flow<UserPreferences> = dataSource.userPreferences
    override suspend fun setDarkTheme(enabled: Boolean) = dataSource.setDarkTheme(enabled)
}
