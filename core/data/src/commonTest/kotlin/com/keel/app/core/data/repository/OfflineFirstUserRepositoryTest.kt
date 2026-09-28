package com.keel.app.core.data.repository

import app.cash.turbine.test
import com.keel.app.core.common.AppDispatchers
import com.keel.app.core.database.dao.UserDao
import com.keel.app.core.database.model.UserEntity
import com.keel.app.core.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class OfflineFirstUserRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()
    private val dao = InMemoryUserDao()
    private val remoteUsers = listOf(User("1", "Ada", "ada@example.com"))
    private val repository = OfflineFirstUserRepository(
        userDao = dao,
        remoteDataSource = { remoteUsers },
        dispatchers = object : AppDispatchers {
            override val io = testDispatcher
            override val default = testDispatcher
            override val main = testDispatcher
        },
    )

    @Test
    fun refresh_storesRemoteUsersLocally() = TestScope(testDispatcher).runTest {
        repository.observeUsers().test {
            assertEquals(emptyList(), awaitItem())
            repository.refresh()
            assertEquals(remoteUsers, awaitItem())
        }
    }

    @Test
    fun observeUser_returnsNullWhenMissing() = TestScope(testDispatcher).runTest {
        repository.observeUser("unknown").test {
            assertNull(awaitItem())
        }
    }
}

private class InMemoryUserDao : UserDao {
    private val users = MutableStateFlow<Map<String, UserEntity>>(emptyMap())

    override fun observeAll(): Flow<List<UserEntity>> = users.map { it.values.sortedBy(UserEntity::name) }
    override fun observeById(id: String): Flow<UserEntity?> = users.map { it[id] }
    override suspend fun upsertAll(users: List<UserEntity>) {
        this.users.value += users.associateBy(UserEntity::id)
    }
}
