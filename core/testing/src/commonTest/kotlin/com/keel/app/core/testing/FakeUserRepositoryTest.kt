package com.keel.app.core.testing

import app.cash.turbine.test
import com.keel.app.core.testing.data.testUsers
import com.keel.app.core.testing.repository.FakeUserRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class FakeUserRepositoryTest {

    @Test
    fun observeUser_findsUserById() = runTest {
        val repository = FakeUserRepository()
        repository.observeUser("2").test {
            repository.sendUsers(testUsers)
            assertEquals(testUsers[1], awaitItem())
        }
    }
}
