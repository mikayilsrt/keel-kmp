package com.keel.app.core.database.di

import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.keel.app.core.database.KeelDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformDatabaseModule: Module = module {
    single<RoomDatabase.Builder<KeelDatabase>> {
        val context = androidContext()
        Room.databaseBuilder<KeelDatabase>(
            context = context,
            name = context.getDatabasePath(KeelDatabase.FILE_NAME).absolutePath,
        )
    }
}
