package com.keel.app.core.data.repository

import com.keel.app.core.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeUsers(): Flow<List<User>>
    fun observeUser(id: String): Flow<User?>

    /** Fetches users from the remote source and stores them locally. */
    suspend fun refresh()
}
