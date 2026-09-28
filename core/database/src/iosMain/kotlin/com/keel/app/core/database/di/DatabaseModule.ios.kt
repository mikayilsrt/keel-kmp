package com.keel.app.core.database.di

import androidx.room3.Room
import androidx.room3.RoomDatabase
import com.keel.app.core.database.KeelDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

internal actual val platformDatabaseModule: Module = module {
    single<RoomDatabase.Builder<KeelDatabase>> {
        Room.databaseBuilder<KeelDatabase>(name = "${documentDirectory()}/${KeelDatabase.FILE_NAME}")
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val url = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(url?.path) { "Documents directory not found" }
}
