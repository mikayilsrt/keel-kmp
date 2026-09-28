package com.keel.app.core.data.source

import com.keel.app.core.model.User

/** Remote source of users. Replace [FakeUserRemoteDataSource] by a Ktor implementation using `core:network`. */
internal fun interface UserRemoteDataSource {
    suspend fun fetchUsers(): List<User>
}
