package com.keel.app.core.testing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

/**
 * KMP replacement for JUnit's `MainDispatcherRule`: runs [testBody] with `Dispatchers.Main`
 * replaced by [testDispatcher], so that `viewModelScope` can be used in common tests.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun runMainTest(
    testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
    testBody: suspend TestScope.() -> Unit,
) {
    Dispatchers.setMain(testDispatcher)
    try {
        runTest(testDispatcher) { testBody() }
    } finally {
        Dispatchers.resetMain()
    }
}
