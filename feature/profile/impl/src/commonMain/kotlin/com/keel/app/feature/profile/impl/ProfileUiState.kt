package com.keel.app.feature.profile.impl

import com.keel.app.core.model.User

internal sealed interface ProfileUiState {
    val userId: String

    data class Loading(override val userId: String) : ProfileUiState
    data class NotFound(override val userId: String) : ProfileUiState
    data class Success(val user: User) : ProfileUiState {
        override val userId: String get() = user.id
    }
}
