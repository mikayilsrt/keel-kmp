package com.keel.app.core.network

import io.ktor.client.engine.HttpClientEngine

/** Platform HTTP engine: OkHttp on Android, Darwin (NSURLSession) on iOS. */
internal expect fun platformHttpClientEngine(): HttpClientEngine
