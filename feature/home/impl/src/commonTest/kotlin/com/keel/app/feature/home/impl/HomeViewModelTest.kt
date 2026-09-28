package com.keel.app.feature.home.impl

import app.cash.turbine.test
import com.keel.app.core.testing.data.testUsers
import com.keel.app.core.testing.repository.FakeUserRepository
import com.keel.app.core.testing.runMainTest
import kotlin.test.Test
import kotlin.test.assertEquals

class HomeViewModelTest {

    private val userRepository = FakeUserRepository()

    @Test
    fun uiState_isLoadingUntilUsersAreAvailable() = runMainTest {
        val viewModel = HomeViewModel(userRepository)
        viewModel.uiState.test {
            assertEquals(HomeUiState.Loading, awaitItem())
            userRepository.sendUsers(testUsers)
            assertEquals(HomeUiState.Success(testUsers), awaitItem())
        }
    }

    @Test
    fun init_refreshesUsers() = runMainTest {
        HomeViewModel(userRepository)
        assertEquals(1, userRepository.refreshCount)
    }

    @Test
    fun uiState_isErrorWhenRefreshFailsWithoutCachedUsers() = runMainTest {
        userRepository.refreshError = IllegalStateException("offline")
        userRepository.sendUsers(emptyList())
        val viewModel = HomeViewModel(userRepository)
        viewModel.uiState.test {
            assertEquals(HomeUiState.Error, awaitItem())
        }
    }

    @Test
    fun uiState_showsCachedUsersWhenRefreshFails() = runMainTest {
        userRepository.refreshError = IllegalStateException("offline")
        userRepository.sendUsers(testUsers)
        val viewModel = HomeViewModel(userRepository)
        viewModel.uiState.test {
            assertEquals(HomeUiState.Success(testUsers), awaitItem())
        }
    }

    @Test
    fun refresh_retriesAfterError() = runMainTest {
        userRepository.refreshError = IllegalStateException("offline")
        userRepository.sendUsers(emptyList())
        val viewModel = HomeViewModel(userRepository)
        viewModel.uiState.test {
            assertEquals(HomeUiState.Error, awaitItem())
            userRepository.refreshError = null
            viewModel.refresh()
            assertEquals(HomeUiState.Loading, awaitItem())
            assertEquals(HomeUiState.Success(emptyList()), awaitItem())
        }
        assertEquals(2, userRepository.refreshCount)
    }
}
