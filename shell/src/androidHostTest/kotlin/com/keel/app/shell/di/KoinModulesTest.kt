package com.keel.app.shell.di

import androidx.lifecycle.SavedStateHandle
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.verify.verify
import kotlin.test.Test

/**
 * Checks that every constructor dependency declared in the Koin graph can be resolved.
 * `verify()` relies on JVM reflection, hence this test lives in the Android host source set.
 */
@OptIn(KoinExperimentalAPI::class)
class KoinModulesTest {

    @Test
    fun allModulesAreResolvable() {
        module {
            includes(coreModules + featureModules + shellModule)
        }.verify(
            // Provided at runtime by the navigation back stack entry.
            extraTypes = listOf(SavedStateHandle::class),
        )
    }
}
