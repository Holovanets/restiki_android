package com.example.restiki_android.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restiki_android.data.api.RequestCodeResponse
import com.example.restiki_android.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private var currentPhone: String = ""

    fun requestCode(phone: String) {
        currentPhone = phone
        viewModelScope.launch {
            _authState.value = AuthState(isLoading = true)
            repository.requestCode(phone).onSuccess {
                _authState.value = AuthState(isSuccess = true)
            }.onFailure {
                _authState.value = AuthState(error = it.message ?: "Помилка")
            }
        }
    }

    fun verifyCode(code: String) {
        viewModelScope.launch {
            _authState.value = AuthState(isLoading = true)
            repository.verifyCode(currentPhone, code).onSuccess {
                _authState.value = AuthState(isSuccess = true)
            }.onFailure {
                _authState.value = AuthState(error = it.message ?: "Щось не так з кодом...")
            }
        }
    }

    fun getCurrentPhone(): String = currentPhone
}

data class AuthState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String = ""
)