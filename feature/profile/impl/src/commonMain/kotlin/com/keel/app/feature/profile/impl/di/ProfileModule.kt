package com.keel.app.feature.profile.impl.di

import com.keel.app.core.navigation.FeatureEntry
import com.keel.app.feature.profile.impl.ProfileEntry
import com.keel.app.feature.profile.impl.ProfileViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

public val profileModule: Module = module {
    factoryOf(::ProfileEntry) bind FeatureEntry::class
    viewModelOf(::ProfileViewModel)
}
