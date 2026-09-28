package com.keel.app.core.common.di

import com.keel.app.core.common.AppDispatchers
import com.keel.app.core.common.DefaultAppDispatchers
import org.koin.dsl.module

val commonModule = module {
    single<AppDispatchers> { DefaultAppDispatchers }
}
