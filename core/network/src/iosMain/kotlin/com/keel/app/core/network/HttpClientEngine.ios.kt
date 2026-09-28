package com.keel.app.core.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

internal actual fun platformHttpClientEngine(): HttpClientEngine = Darwin.create()
