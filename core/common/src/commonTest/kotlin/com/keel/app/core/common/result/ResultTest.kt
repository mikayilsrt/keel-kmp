package com.keel.app.core.common.result

import app.cash.turbine.test
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ResultTest {

    @Test
    fun asResult_emitsLoadingThenSuccess() = runTest {
        flowOf(1).asResult().test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success(1), awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun asResult_catchesErrors() = runTest {
        flow<Int> { error("boom") }.asResult().test {
            assertEquals(Result.Loading, awaitItem())
            assertIs<Result.Error>(awaitItem())
            awaitComplete()
        }
    }
}
