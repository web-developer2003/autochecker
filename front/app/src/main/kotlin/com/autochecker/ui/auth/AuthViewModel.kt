package com.autochecker.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autochecker.core.network.NetworkResult
import com.autochecker.data.model.User
import com.autochecker.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
)

data class SignUpState(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _signUpState = MutableStateFlow(SignUpState())
    val signUpState: StateFlow<SignUpState> = _signUpState.asStateFlow()

    fun updateLoginEmail(email: String) {
        _loginState.value = _loginState.value.copy(email = email, error = null)
    }

    fun updateLoginPassword(password: String) {
        _loginState.value = _loginState.value.copy(password = password, error = null)
    }

    fun login() {
        val state = _loginState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _loginState.value = state.copy(error = "Barcha maydonlarni to'ldiring")
            return
        }
        viewModelScope.launch {
            _loginState.value = state.copy(isLoading = true, error = null)
            when (val result = authRepository.login(state.email, state.password)) {
                is NetworkResult.Success -> {
                    _loginState.value = _loginState.value.copy(isLoading = false, isSuccess = true)
                }
                is NetworkResult.Error -> {
                    _loginState.value = _loginState.value.copy(isLoading = false, error = result.message)
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun updateSignUpFullName(name: String) {
        _signUpState.value = _signUpState.value.copy(fullName = name, error = null)
    }

    fun updateSignUpEmail(email: String) {
        _signUpState.value = _signUpState.value.copy(email = email, error = null)
    }

    fun updateSignUpPhone(phone: String) {
        _signUpState.value = _signUpState.value.copy(phone = phone, error = null)
    }

    fun updateSignUpPassword(password: String) {
        _signUpState.value = _signUpState.value.copy(password = password, error = null)
    }

    fun updateSignUpConfirmPassword(password: String) {
        _signUpState.value = _signUpState.value.copy(confirmPassword = password, error = null)
    }

    fun signUp() {
        val state = _signUpState.value
        if (state.fullName.isBlank() || state.email.isBlank() || state.password.isBlank()) {
            _signUpState.value = state.copy(error = "Barcha maydonlarni to'ldiring")
            return
        }
        if (state.password != state.confirmPassword) {
            _signUpState.value = state.copy(error = "Parollar mos kelmaydi")
            return
        }
        if (state.password.length < 6) {
            _signUpState.value = state.copy(error = "Parol kamida 6 ta belgidan iborat bo'lishi kerak")
            return
        }
        viewModelScope.launch {
            _signUpState.value = state.copy(isLoading = true, error = null)
            when (val result = authRepository.register(state.fullName, state.email, state.phone, state.password)) {
                is NetworkResult.Success -> {
                    _signUpState.value = _signUpState.value.copy(isLoading = false, isSuccess = true)
                }
                is NetworkResult.Error -> {
                    _signUpState.value = _signUpState.value.copy(isLoading = false, error = result.message)
                }
                is NetworkResult.Loading -> {}
            }
        }
    }
}
