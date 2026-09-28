package com.keel.app.core.database

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import com.keel.app.core.database.dao.UserDao
import com.keel.app.core.database.model.UserEntity

@Database(entities = [UserEntity::class], version = 1)
@ConstructedBy(KeelDatabaseConstructor::class)
abstract class KeelDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    internal companion object {
        const val FILE_NAME = "keel.db"
    }
}

/** Implemented by the Room compiler for each target. */
@Suppress("KotlinNoActualForExpect")
internal expect object KeelDatabaseConstructor : RoomDatabaseConstructor<KeelDatabase> {
    override fun initialize(): KeelDatabase
}
