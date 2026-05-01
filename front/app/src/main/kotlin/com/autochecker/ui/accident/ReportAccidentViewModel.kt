package com.autochecker.ui.accident

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autochecker.core.network.NetworkResult
import com.autochecker.data.repository.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReportAccidentState(
    val plateNumber: String = "",
    val description: String = "",
    val severity: String = "minor",
    val location: String = "",
    val photoUri: Uri? = null,
    val isSubmitting: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val successMessage: String? = null,
)

@HiltViewModel
class ReportAccidentViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(ReportAccidentState())
    val state: StateFlow<ReportAccidentState> = _state.asStateFlow()

    fun updatePlateNumber(value: String) {
        _state.value = _state.value.copy(plateNumber = value.uppercase().take(20), error = null)
    }

    fun updateDescription(value: String) {
        _state.value = _state.value.copy(description = value, error = null)
    }

    fun updateSeverity(value: String) {
        _state.value = _state.value.copy(severity = value)
    }

    fun updateLocation(value: String) {
        _state.value = _state.value.copy(location = value)
    }

    fun updatePhotoUri(uri: Uri?) {
        _state.value = _state.value.copy(photoUri = uri, error = null)
    }

    fun submit() {
        val s = _state.value
        if (s.plateNumber.isBlank()) {
            _state.value = s.copy(error = "Davlat raqamini kiriting")
            return
        }
        if (s.photoUri == null) {
            _state.value = s.copy(error = "Suratni tanlang yoki oling")
            return
        }

        viewModelScope.launch {
            _state.value = _state.value.copy(isSubmitting = true, error = null)
            when (val result = vehicleRepository.reportAccident(
                plateNumber = s.plateNumber,
                photoUri = s.photoUri,
                description = s.description,
                severity = s.severity,
                location = s.location,
            )) {
                is NetworkResult.Success -> {
                    _state.value = _state.value.copy(
                        isSubmitting = false,
                        success = true,
                        successMessage = result.data.message,
                    )
                }
                is NetworkResult.Error -> {
                    _state.value = _state.value.copy(
                        isSubmitting = false,
                        error = result.message,
                    )
                }
                is NetworkResult.Loading -> {}
            }
        }
    }
}
