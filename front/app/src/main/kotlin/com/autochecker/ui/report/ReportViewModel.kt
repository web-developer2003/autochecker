package com.autochecker.ui.report

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.autochecker.core.network.NetworkResult
import com.autochecker.data.model.Accident
import com.autochecker.data.model.VehicleReport
import com.autochecker.data.repository.VehicleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ReportState(
    val report: VehicleReport? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false,
)

data class AccidentDetailState(
    val accident: Accident? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val vehicleRepository: VehicleRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _reportState = MutableStateFlow(ReportState())
    val reportState: StateFlow<ReportState> = _reportState.asStateFlow()

    private val _accidentDetailState = MutableStateFlow(AccidentDetailState())
    val accidentDetailState: StateFlow<AccidentDetailState> = _accidentDetailState.asStateFlow()

    init {
        val vin = savedStateHandle.get<String>("vin")
        val accidentId = savedStateHandle.get<String>("accidentId")
        if (vin != null) loadReport(vin)
        if (accidentId != null) loadAccidentDetail(accidentId)
    }

    fun loadReport(vin: String) {
        viewModelScope.launch {
            _reportState.value = ReportState(isLoading = true)
            when (val result = vehicleRepository.checkVehicle(query = vin, searchType = "vin", source = "local")) {
                is NetworkResult.Success -> {
                    _reportState.value = ReportState(
                        report = result.data,
                        isSaved = result.data.vehicle.isSaved,
                    )
                }
                is NetworkResult.Error -> {
                    _reportState.value = ReportState(error = result.message)
                }
                is NetworkResult.Loading -> {}
            }
        }
    }

    fun saveVehicle() {
        val report = _reportState.value.report ?: return
        val vehicle = report.vehicle
        viewModelScope.launch {
            val result = vehicleRepository.saveVehicle(
                vin = vehicle.vin,
                plateNumber = vehicle.plateNumber,
                name = "${vehicle.make} ${vehicle.model}",
            )
            if (result is NetworkResult.Success) {
                _reportState.value = _reportState.value.copy(isSaved = true)
            }
        }
    }

    fun loadAccidentDetail(accidentId: String) {
        viewModelScope.launch {
            _accidentDetailState.value = AccidentDetailState(isLoading = true)
            when (val result = vehicleRepository.getAccidentDetail(accidentId)) {
                is NetworkResult.Success -> {
                    _accidentDetailState.value = AccidentDetailState(accident = result.data)
                }
                is NetworkResult.Error -> {
                    _accidentDetailState.value = AccidentDetailState(error = result.message)
                }
                is NetworkResult.Loading -> {}
            }
        }
    }
}
