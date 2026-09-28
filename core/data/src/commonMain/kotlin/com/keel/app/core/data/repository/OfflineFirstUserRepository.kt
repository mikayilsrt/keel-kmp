package com.keel.app.core.data.repository

import com.keel.app.core.common.AppDispatchers
import com.keel.app.core.data.mapper.asEntity
import com.keel.app.core.data.mapper.asExternalModel
import com.keel.app.core.data.source.UserRemoteDataSource
import com.keel.app.core.database.dao.UserDao
import com.keel.app.core.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/** Room is the single source of truth; the remote source only feeds it. */
internal class OfflineFirstUserRepository(
    private val userDao: UserDao,
    private val remoteDataSource: UserRemoteDataSource,
    private val dispatchers: AppDispatchers,
) : UserRepository {

    override fun observeUsers(): Flow<List<User>> =
        userDao.observeAll().map { users -> users.map { it.asExternalModel() } }

    override fun observeUser(id: String): Flow<User?> =
        userDao.observeById(id).map { it?.asExternalModel() }

    override suspend fun refresh() = withContext(dispatchers.io) {
        userDao.upsertAll(remoteDataSource.fetchUsers().map { it.asEntity() })
    }
}
