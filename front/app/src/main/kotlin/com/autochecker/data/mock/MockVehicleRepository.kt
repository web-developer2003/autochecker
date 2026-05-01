package com.autochecker.data.mock

import android.net.Uri
import com.autochecker.core.network.NetworkResult
import com.autochecker.data.model.*
import com.autochecker.data.repository.VehicleRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockVehicleRepository @Inject constructor() : VehicleRepository {
    private val savedVehicles = MockData.savedVehicles.toMutableList()

    override suspend fun checkVehicle(
        query: String,
        searchType: String,
        source: String,
    ): NetworkResult<VehicleReport> {
        delay(1500)
        return NetworkResult.Success(MockData.vehicleReport)
    }

    override suspend fun searchVehicleOnly(
        query: String,
        searchType: String,
        source: String,
    ): NetworkResult<SearchResult> {
        delay(500)
        return NetworkResult.Success(
            SearchResult(
                vehicle = MockData.vehicleReport.vehicle,
                checkGuid = "mock-guid",
                source = source,
            )
        )
    }

    override suspend fun getSearchHistory(): NetworkResult<List<SearchHistoryItem>> {
        delay(500)
        return NetworkResult.Success(MockData.searchHistory)
    }

    override suspend fun getSavedVehicles(): NetworkResult<List<Vehicle>> {
        delay(500)
        return NetworkResult.Success(savedVehicles.toList())
    }

    override suspend fun saveVehicle(vin: String, plateNumber: String, name: String): NetworkResult<Vehicle> {
        delay(500)
        val vehicle = Vehicle(
            vin = vin,
            plate = plateNumber,
            make = name.split(" ").firstOrNull() ?: "",
            model = name.split(" ").drop(1).joinToString(" "),
            isSaved = true,
        )
        savedVehicles.add(vehicle)
        return NetworkResult.Success(vehicle)
    }

    override suspend fun deleteSavedVehicle(id: String): NetworkResult<Unit> {
        delay(300)
        savedVehicles.removeAll { it.guid == id }
        return NetworkResult.Success(Unit)
    }

    override suspend fun getAccidentDetail(accidentId: String): NetworkResult<Accident> {
        delay(500)
        val accident = MockData.accidents.find { it.guid == accidentId }
        return if (accident != null) {
            NetworkResult.Success(accident)
        } else {
            NetworkResult.Error("Avariya topilmadi")
        }
    }

    override suspend fun reportAccident(
        plateNumber: String,
        photoUri: Uri,
        description: String,
        severity: String,
        location: String,
    ): NetworkResult<AccidentReportResponse> {
        delay(1000)
        return NetworkResult.Success(
            AccidentReportResponse(
                message = "Avariya muvaffaqiyatli ro'yxatga olindi",
                accidentGuid = "mock-accident-guid",
                vehicleGuid = "mock-vehicle-guid",
                vehicleCreated = false,
            )
        )
    }
}
