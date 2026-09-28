package com.keel.app.core.data.source

import com.keel.app.core.model.User
import kotlinx.coroutines.delay

/** Simulates a backend with a small latency. */
internal class FakeUserRemoteDataSource : UserRemoteDataSource {
    override suspend fun fetchUsers(): List<User> {
        delay(LATENCY_MS)
        return NAMES.mapIndexed { index, name ->
            User(
                id = (index + 1).toString(),
                name = name,
                email = "${name.substringBefore(' ').lowercase()}@example.com",
            )
        }
    }

    private companion object {
        const val LATENCY_MS = 500L
        val NAMES = listOf(
            "Ada Lovelace", "Alan Turing", "Barbara Liskov", "Donald Knuth", "Edsger Dijkstra",
            "Grace Hopper", "John McCarthy", "Ken Thompson", "Linus Torvalds", "Margaret Hamilton",
        )
    }
}
