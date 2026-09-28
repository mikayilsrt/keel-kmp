package com.keel.app.feature.home.impl

import com.keel.app.core.model.User

internal sealed interface HomeUiState {
    data object Loading : HomeUiState
    data object Error : HomeUiState
    data class Success(val users: List<User>) : HomeUiState
}
