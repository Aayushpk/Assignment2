package com.example.assignment1.ui

import com.example.assignment1.MainDispatcherRule
import com.example.assignment1.data.model.DashboardResponse
import com.example.assignment1.data.model.Entity
import com.example.assignment1.data.repository.AuthRepository
import com.example.assignment1.ui.dashboard.DashboardViewModel
import com.example.assignment1.util.Resource
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: AuthRepository = mockk()
    private lateinit var viewModel: DashboardViewModel

    @Test
    fun `successful load populates entities and total`() = runTest {
        val entities = listOf(
            Entity("Stock", "AAPL", 150.25, 0.65, "desc1"),
            Entity("Stock", "MSFT", 320.10, 0.80, "desc2")
        )
        coEvery { repository.getDashboard("topic123") } returns
            Resource.Success(DashboardResponse(entities, entityTotal = 2))
        viewModel = DashboardViewModel(repository)

        viewModel.loadDashboard("topic123")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(2, state.entities.size)
        assertEquals(2, state.entityTotal)
        assertEquals("AAPL", state.entities.first().ticker)
    }

    @Test
    fun `error load exposes message and empty list`() = runTest {
        coEvery { repository.getDashboard(any()) } returns
            Resource.Error("Network error: timeout")
        viewModel = DashboardViewModel(repository)

        viewModel.loadDashboard("topic123")
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("Network error: timeout", state.error)
        assertTrue(state.entities.isEmpty())
    }
}
