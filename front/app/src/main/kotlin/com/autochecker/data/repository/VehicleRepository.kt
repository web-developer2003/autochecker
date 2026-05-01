package com.autochecker.data.repository

import android.net.Uri
import com.autochecker.core.network.NetworkResult
import com.autochecker.data.model.*

interface VehicleRepository {
    suspend fun checkVehicle(
        query: String,
        searchType: String = "plate",
        source: String = "local",
    ): NetworkResult<VehicleReport>

    suspend fun searchVehicleOnly(
        query: String,
        searchType: String = "plate",
        source: String = "local",
    ): NetworkResult<SearchResult>

    suspend fun getSearchHistory(): NetworkResult<List<SearchHistoryItem>>
    suspend fun getSavedVehicles(): NetworkResult<List<Vehicle>>
    suspend fun saveVehicle(vin: String, plateNumber: String, name: String): NetworkResult<Vehicle>
    suspend fun deleteSavedVehicle(id: String): NetworkResult<Unit>
    suspend fun getAccidentDetail(accidentId: String): NetworkResult<Accident>

    suspend fun reportAccident(
        plateNumber: String,
        photoUri: Uri,
        description: String,
        severity: String,
        location: String,
    ): NetworkResult<AccidentReportResponse>
}
