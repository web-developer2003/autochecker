package com.autochecker.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autochecker.core.network.NetworkResult
import com.autochecker.data.model.User
import com.autochecker.data.repository.AuthRepository
import com.autochecker.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class EditProfileState(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false,
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    private val _editState = MutableStateFlow(EditProfileState())
    val editState: StateFlow<EditProfileState> = _editState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = ProfileState(isLoading = true)
            when (val result = userRepository.getProfile()) {
                is NetworkResult.Success -> {
                    _profileState.value = ProfileState(user = result.data)
                    _editState.value = EditProfileState(
                        fullName = result.data.fullName,
                        email = result.data.email,
                        phone = result.data.phone,
                    )
                }
                is NetworkResult.Error -> {
                    _profileState.value = ProfileState(error = result.message)
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun updateFullName(name: String) {
        _editState.value = _editState.value.copy(fullName = name, error = null)
    }

    fun updateEmail(email: String) {
        _editState.value = _editState.value.copy(email = email, error = null)
    }

    fun updatePhone(phone: String) {
        _editState.value = _editState.value.copy(phone = phone, error = null)
    }

    fun saveProfile() {
        val state = _editState.value
        if (state.fullName.isBlank() || state.email.isBlank()) {
            _editState.value = state.copy(error = "Ism va email to'ldirilishi shart")
            return
        }
        viewModelScope.launch {
            _editState.value = state.copy(isLoading = true, error = null)
            when (val result = userRepository.updateProfile(state.fullName, state.email, state.phone)) {
                is NetworkResult.Success -> {
                    _editState.value = _editState.value.copy(isLoading = false, isSaved = true)
                    _profileState.value = ProfileState(user = result.data)
                }
                is NetworkResult.Error -> {
                    _editState.value = _editState.value.copy(isLoading = false, error = result.message)
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
