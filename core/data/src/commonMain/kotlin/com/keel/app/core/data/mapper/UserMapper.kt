package com.keel.app.core.data.mapper

import com.keel.app.core.database.model.UserEntity
import com.keel.app.core.model.User

internal fun UserEntity.asExternalModel() = User(id = id, name = name, email = email)

internal fun User.asEntity() = UserEntity(id = id, name = name, email = email)
