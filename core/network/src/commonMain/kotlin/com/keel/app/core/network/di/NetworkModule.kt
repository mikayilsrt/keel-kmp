package com.keel.app.core.network.di

import com.keel.app.core.network.KeelJson
import com.keel.app.core.network.createHttpClient
import com.keel.app.core.network.platformHttpClientEngine
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single<Json> { KeelJson }
    single<HttpClient> { createHttpClient(engine = platformHttpClientEngine(), json = get()) }
}
