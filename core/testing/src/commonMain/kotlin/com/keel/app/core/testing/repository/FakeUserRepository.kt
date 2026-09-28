package com.keel.app.core.testing.repository

import com.keel.app.core.data.repository.UserRepository
import com.keel.app.core.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map

/** Test double of [UserRepository]. Nothing is emitted until [sendUsers] is called. */
class FakeUserRepository : UserRepository {
    private val users = MutableSharedFlow<List<User>>(replay = 1)

    /** Error thrown by [refresh], if any. */
    var refreshError: Throwable? = null
    var refreshCount: Int = 0
        private set

    fun sendUsers(value: List<User>) {
        users.tryEmit(value)
    }

    override fun observeUsers(): Flow<List<User>> = users

    override fun observeUser(id: String): Flow<User?> = users.map { list -> list.find { it.id == id } }

    override suspend fun refresh() {
        refreshCount++
        refreshError?.let { throw it }
    }
}
