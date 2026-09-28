package com.keel.app.core.network

import io.ktor.client.call.body
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class HttpClientFactoryTest {

    @Serializable
    private data class Payload(val id: String)

    @Test
    fun decodesJsonAndIgnoresUnknownKeys() = runTest {
        val engine = MockEngine {
            respond(
                content = """{"id":"42","unknown":true}""",
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        val payload: Payload = createHttpClient(engine).get("https://example.com").body()
        assertEquals(Payload("42"), payload)
    }

    @Test
    fun failsOnErrorStatus() = runTest {
        val engine = MockEngine { respond(content = "", status = HttpStatusCode.NotFound) }
        assertFailsWith<Exception> { createHttpClient(engine).get("https://example.com") }
    }
}
