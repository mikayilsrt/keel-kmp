package com.keel.app.feature.profile.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.keel.app.core.data.repository.UserRepository
import com.keel.app.feature.profile.api.ProfileRoute
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

internal class ProfileViewModel(
    savedStateHandle: SavedStateHandle,
    userRepository: UserRepository,
) : ViewModel() {

    private val userId: String = savedStateHandle.toRoute<ProfileRoute>().userId

    val uiState: StateFlow<ProfileUiState> = userRepository.observeUser(userId)
        .map { user -> if (user == null) ProfileUiState.NotFound(userId) else ProfileUiState.Success(user) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ProfileUiState.Loading(userId),
        )
}
