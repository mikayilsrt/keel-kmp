package com.keel.app.core.data.di

import com.keel.app.core.data.repository.DefaultUserPreferencesRepository
import com.keel.app.core.data.repository.OfflineFirstUserRepository
import com.keel.app.core.data.repository.UserPreferencesRepository
import com.keel.app.core.data.repository.UserRepository
import com.keel.app.core.data.source.FakeUserRemoteDataSource
import com.keel.app.core.data.source.UserRemoteDataSource
import com.keel.app.core.database.di.databaseModule
import com.keel.app.core.datastore.di.dataStoreModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    includes(databaseModule, dataStoreModule)
    singleOf(::FakeUserRemoteDataSource) bind UserRemoteDataSource::class
    singleOf(::OfflineFirstUserRepository) bind UserRepository::class
    singleOf(::DefaultUserPreferencesRepository) bind UserPreferencesRepository::class
}
