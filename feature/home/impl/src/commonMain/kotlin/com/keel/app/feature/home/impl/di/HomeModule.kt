package com.keel.app.feature.home.impl.di

import com.keel.app.core.navigation.FeatureEntry
import com.keel.app.feature.home.impl.HomeEntry
import com.keel.app.feature.home.impl.HomeViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val homeModule: Module = module {
    factoryOf(::HomeEntry) bind FeatureEntry::class
    viewModelOf(::HomeViewModel)
}
