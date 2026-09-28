package com.keel.app.feature.settings.impl.di

import com.keel.app.core.navigation.FeatureEntry
import com.keel.app.feature.settings.impl.SettingsEntry
import com.keel.app.feature.settings.impl.SettingsViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val settingsModule: Module = module {
    factoryOf(::SettingsEntry) bind FeatureEntry::class
    viewModelOf(::SettingsViewModel)
}
