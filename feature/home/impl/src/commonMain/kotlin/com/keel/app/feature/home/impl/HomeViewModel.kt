package com.keel.app.feature.home.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keel.app.core.common.suspendRunCatching
import com.keel.app.core.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class HomeViewModel(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val refreshStatus = MutableStateFlow(RefreshStatus.Running)

    val uiState: StateFlow<HomeUiState> =
        combine(userRepository.observeUsers(), refreshStatus) { users, status ->
            when {
                users.isNotEmpty() -> HomeUiState.Success(users)
                status == RefreshStatus.Running -> HomeUiState.Loading
                status == RefreshStatus.Failed -> HomeUiState.Error
                else -> HomeUiState.Success(emptyList())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState.Loading,
        )

    init {
        refresh()
    }

    fun refresh() {
        refreshStatus.value = RefreshStatus.Running
        viewModelScope.launch {
            refreshStatus.value = suspendRunCatching { userRepository.refresh() }
                .fold(onSuccess = { RefreshStatus.Done }, onFailure = { RefreshStatus.Failed })
        }
    }

    private enum class RefreshStatus { Running, Done, Failed }
}
