package com.keel.app.core.common

import kotlin.coroutines.cancellation.CancellationException

/** Like [runCatching] but rethrows [CancellationException] so structured concurrency keeps working. */
suspend inline fun <T> suspendRunCatching(crossinline block: suspend () -> T): kotlin.Result<T> =
    try {
        kotlin.Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        kotlin.Result.failure(e)
    }
