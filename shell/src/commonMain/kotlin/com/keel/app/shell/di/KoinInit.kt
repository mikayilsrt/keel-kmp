package com.keel.app.shell.di

import com.keel.app.core.common.di.commonModule
import com.keel.app.core.data.di.dataModule
import com.keel.app.core.network.di.networkModule
import com.keel.app.feature.home.impl.di.homeModule
import com.keel.app.feature.profile.impl.di.profileModule
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

internal val coreModules: List<Module> = listOf(
    commonModule,
    networkModule,
    dataModule,
)

internal val featureModules: List<Module> = listOf(
    homeModule,
    profileModule,
)

/**
 * Starts Koin with every core and feature module. Must be called exactly once per process:
 * from `Application.onCreate` on Android, from the SwiftUI `App` initializer on iOS.
 */
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(coreModules + featureModules)
    }
}
