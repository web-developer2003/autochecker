package com.autochecker.ui.vehicles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autochecker.core.network.NetworkResult
import com.autochecker.data.model.Vehicle
import com.autochecker.data.repository.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SavedVehiclesState(
    val vehicles: List<Vehicle> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class AddVehicleState(
    val vin: String = "",
    val plateNumber: String = "",
    val name: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false,
)

@HiltViewModel
class VehiclesViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
) : ViewModel() {

    private val _savedState = MutableStateFlow(SavedVehiclesState())
    val savedState: StateFlow<SavedVehiclesState> = _savedState.asStateFlow()

    private val _addState = MutableStateFlow(AddVehicleState())
    val addState: StateFlow<AddVehicleState> = _addState.asStateFlow()

    init {
        loadSavedVehicles()
    }

    fun loadSavedVehicles() {
        viewModelScope.launch {
            _savedState.value = SavedVehiclesState(isLoading = true)
            when (val result = vehicleRepository.getSavedVehicles()) {
                is NetworkResult.Success -> {
                    _savedState.value = SavedVehiclesState(vehicles = result.data)
                }
                is NetworkResult.Error -> {
                    _savedState.value = SavedVehiclesState(error = result.message)
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun deleteVehicle(id: String) {
        viewModelScope.launch {
            vehicleRepository.deleteSavedVehicle(id)
            loadSavedVehicles()
        }
    }

    fun updateVin(vin: String) {
        _addState.value = _addState.value.copy(vin = vin.uppercase().take(17), error = null)
    }

    fun updatePlateNumber(plate: String) {
        _addState.value = _addState.value.copy(plateNumber = plate, error = null)
    }

    fun updateName(name: String) {
        _addState.value = _addState.value.copy(name = name, error = null)
    }

    fun addVehicle() {
        val state = _addState.value
        if (state.vin.isBlank() || state.name.isBlank()) {
            _addState.value = state.copy(error = "VIN va nom to'ldirilishi shart")
            return
        }
        viewModelScope.launch {
            _addState.value = state.copy(isLoading = true, error = null)
            when (val result = vehicleRepository.saveVehicle(state.vin, state.plateNumber, state.name)) {
                is NetworkResult.Success -> {
                    _addState.value = _addState.value.copy(isLoading = false, isSaved = true)
                }
                is NetworkResult.Error -> {
                    _addState.value = _addState.value.copy(isLoading = false, error = result.message)
                }
                is NetworkResult.Loading -> {}
            }
        }
    }
}
