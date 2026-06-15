package com.example.assignment1.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment1.data.model.Entity
import com.example.assignment1.data.repository.AuthRepository
import com.example.assignment1.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val isLoading: Boolean = false,
    val entities: List<Entity> = emptyList(),
    val entityTotal: Int = 0,
    val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun loadDashboard(keypass: String) {
        _uiState.value = DashboardUiState(isLoading = true)
        viewModelScope.launch {
            when (val result = repository.getDashboard(keypass)) {
                is Resource.Success -> _uiState.value = DashboardUiState(
                    entities = result.data.entities,
                    entityTotal = result.data.entityTotal
                )
                is Resource.Error -> _uiState.value = DashboardUiState(error = result.message)
            }
        }
    }
}
