package com.example.assignment1.ui

import com.example.assignment1.MainDispatcherRule
import com.example.assignment1.data.model.LoginResponse
import com.example.assignment1.data.repository.AuthRepository
import com.example.assignment1.ui.login.LoginViewModel
import com.example.assignment1.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: AuthRepository = mockk()
    private lateinit var viewModel: LoginViewModel

    @Test
    fun `successful login exposes keypass in state`() = runTest {
        // Given the repository returns a successful login
        coEvery { repository.login(any(), any(), any()) } returns
            Resource.Success(LoginResponse(keypass = "topic123"))
        viewModel = LoginViewModel(repository)

        // When
        viewModel.login("sydney", "s8140991", "Aayush")
        advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertEquals("topic123", state.keypass)
        assertEquals(false, state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `failed login exposes error message`() = runTest {
        coEvery { repository.login(any(), any(), any()) } returns
            Resource.Error("Invalid username or password")
        viewModel = LoginViewModel(repository)

        viewModel.login("sydney", "s8140991", "wrong")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Invalid username or password", state.error)
        assertNull(state.keypass)
    }

    @Test
    fun `blank input is rejected without calling repository`() = runTest {
        viewModel = LoginViewModel(repository)

        viewModel.login("sydney", "", "")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Please enter username and password", state.error)
        assertNull(state.keypass)
    }
}
