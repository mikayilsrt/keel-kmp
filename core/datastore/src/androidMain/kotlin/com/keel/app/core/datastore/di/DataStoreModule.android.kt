package com.keel.app.core.datastore.di

import com.keel.app.core.datastore.DataStorePathProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformDataStoreModule: Module = module {
    single<DataStorePathProvider> {
        val filesDir = androidContext().filesDir
        DataStorePathProvider { fileName -> filesDir.resolve(fileName).absolutePath }
    }
}
