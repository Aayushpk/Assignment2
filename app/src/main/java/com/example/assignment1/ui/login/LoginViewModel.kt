package com.example.assignment1.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment1.data.repository.AuthRepository
import com.example.assignment1.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val isLoading: Boolean = false,
    val keypass: String? = null,   // non-null => login succeeded
    val error: String? = null      // non-null => show error
)


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()


    fun login(campus: String, username: String, password: String) {
        if (username.isBlank() || password.isBlank()) {
            _uiState.value = LoginUiState(error = "Please enter username and password")
            return
        }
        _uiState.value = LoginUiState(isLoading = true)
        viewModelScope.launch {
            when (val result = repository.login(campus, username, password)) {
                is Resource.Success ->
                    _uiState.value = LoginUiState(keypass = result.data.keypass)
                is Resource.Error ->
                    _uiState.value = LoginUiState(error = result.message)
            }
        }
    }


    fun errorShown() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}
