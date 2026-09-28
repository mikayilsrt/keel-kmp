package com.keel.app.core.testing

import com.keel.app.core.common.AppDispatchers
import kotlinx.coroutines.CoroutineDispatcher

class TestAppDispatchers(dispatcher: CoroutineDispatcher) : AppDispatchers {
    override val io: CoroutineDispatcher = dispatcher
    override val default: CoroutineDispatcher = dispatcher
    override val main: CoroutineDispatcher = dispatcher
}
