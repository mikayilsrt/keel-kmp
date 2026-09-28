package com.keel.app.core.datastore.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.keel.app.core.datastore.DataStorePathProvider
import com.keel.app.core.datastore.UserPreferencesDataSource
import okio.Path.Companion.toPath
import org.koin.core.module.Module
import org.koin.dsl.module

private const val USER_PREFERENCES_FILE = "user_preferences.preferences_pb"

/** Provides the platform [DataStorePathProvider]. */
internal expect val platformDataStoreModule: Module

val dataStoreModule = module {
    includes(platformDataStoreModule)
    single<DataStore<Preferences>> {
        val pathProvider = get<DataStorePathProvider>()
        PreferenceDataStoreFactory.createWithPath(
            produceFile = { pathProvider.absolutePath(USER_PREFERENCES_FILE).toPath() },
        )
    }
    single { UserPreferencesDataSource(get()) }
}
