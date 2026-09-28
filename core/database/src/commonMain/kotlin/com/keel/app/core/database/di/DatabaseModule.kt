package com.keel.app.core.database.di

import androidx.room3.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.keel.app.core.common.AppDispatchers
import com.keel.app.core.database.KeelDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

/** Provides a platform-specific `RoomDatabase.Builder<KeelDatabase>` (file location, context). */
internal expect val platformDatabaseModule: Module

val databaseModule = module {
    includes(platformDatabaseModule)
    single<KeelDatabase> {
        get<RoomDatabase.Builder<KeelDatabase>>()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(get<AppDispatchers>().io)
            .build()
    }
    single { get<KeelDatabase>().userDao() }
}
