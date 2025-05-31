package com.example.restiki_android.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.restiki_android.data.api.RequestCodeResponse
import com.example.restiki_android.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private var currentPhone: String = ""

    fun requestCode(phone: String) {
        currentPhone = phone
        println("AuthViewModel: requestCode called with phone=$phone")
        viewModelScope.launch {
            _authState.value = AuthState(isLoading = true)
            repository.requestCode(phone).onSuccess {
                _authState.value = AuthState(isCodeRequested = true) // Код запрошен, переходим к CodeScreen
            }.onFailure {
                _authState.value = AuthState(error = it.message ?: "Помилка")
            }
        }
    }

    fun verifyCode(code: String) {
        println("AuthViewModel: verifyCode called with phone=$currentPhone, code=$code")
        viewModelScope.launch {
            _authState.value = AuthState(isLoading = true)
            repository.verifyCode(currentPhone, code).onSuccess {
                _authState.value = AuthState(isAuthenticated = true) // Успешная авторизация, токены получены
            }.onFailure {
                _authState.value = AuthState(error = it.message ?: "Щось не так з кодом...")
            }
        }
    }

    fun getCurrentPhone(): String = currentPhone
}

data class AuthState(
    val isLoading: Boolean = false,
    val isCodeRequested: Boolean = false, // Код успешно запрошен
    val isAuthenticated: Boolean = false, // Авторизация успешна (код подтвержден, токены получены)
    val error: String = ""
)