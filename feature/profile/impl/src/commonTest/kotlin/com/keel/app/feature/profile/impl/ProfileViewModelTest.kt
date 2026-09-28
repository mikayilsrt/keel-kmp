package com.keel.app.feature.profile.impl

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.keel.app.core.testing.IgnoreOnAndroidHost
import com.keel.app.core.testing.data.testUsers
import com.keel.app.core.testing.repository.FakeUserRepository
import com.keel.app.core.testing.runMainTest
import kotlin.test.Test
import kotlin.test.assertEquals

// toRoute() decodes arguments through android.os.Bundle, which is not available on the Android host.
@IgnoreOnAndroidHost
class ProfileViewModelTest {

    private val userRepository = FakeUserRepository()

    private fun createViewModel(userId: String) = ProfileViewModel(
        savedStateHandle = SavedStateHandle(mapOf("userId" to userId)),
        userRepository = userRepository,
    )

    @Test
    fun uiState_readsUserIdFromRoute() = runMainTest {
        val viewModel = createViewModel(userId = "2")
        viewModel.uiState.test {
            assertEquals(ProfileUiState.Loading(userId = "2"), awaitItem())
            userRepository.sendUsers(testUsers)
            assertEquals(ProfileUiState.Success(testUsers[1]), awaitItem())
        }
    }

    @Test
    fun uiState_isNotFoundForUnknownUser() = runMainTest {
        val viewModel = createViewModel(userId = "unknown")
        viewModel.uiState.test {
            assertEquals(ProfileUiState.Loading(userId = "unknown"), awaitItem())
            userRepository.sendUsers(testUsers)
            assertEquals(ProfileUiState.NotFound(userId = "unknown"), awaitItem())
        }
    }
}
