package com.autochecker.data.repository.impl

import android.content.Context
import android.net.Uri
import com.autochecker.core.network.ApiService
import com.autochecker.core.network.NetworkResult
import com.autochecker.data.model.Accident
import com.autochecker.data.model.AccidentReportResponse
import com.autochecker.data.model.SearchHistoryItem
import com.autochecker.data.model.SearchResult
import com.autochecker.data.model.Vehicle
import com.autochecker.data.model.VehicleReport
import com.autochecker.data.repository.VehicleRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class RealVehicleRepository @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context,
) : VehicleRepository {

    override suspend fun checkVehicle(
        query: String,
        searchType: String,
        source: String,
    ): NetworkResult<VehicleReport> {
        return try {
            val searchResponse = apiService.searchVehicle(
                mapOf("query" to query, "search_type" to searchType, "source" to source)
            )
            if (!searchResponse.isSuccessful) {
                return NetworkResult.Error("Vehicle not found", code = searchResponse.code())
            }

            val searchResult = searchResponse.body()!!

            // Internet search: Gemini returns a text summary, not a DB record
            if (source == "internet") {
                val summary = searchResult.internetSummary
                    ?: searchResult.message
                    ?: "Internet qidiruv natijasi topilmadi"
                return NetworkResult.Error(summary)
            }

            val checkGuid = searchResult.checkGuid
                ?: return NetworkResult.Error("No report available for this vehicle")

            val reportResponse = apiService.getFullReport(checkGuid)
            if (reportResponse.isSuccessful) {
                NetworkResult.Success(reportResponse.body()!!)
            } else {
                NetworkResult.Error("Failed to load report", code = reportResponse.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(message = e.message ?: "Network error")
        }
    }

    override suspend fun searchVehicleOnly(
        query: String,
        searchType: String,
        source: String,
    ): NetworkResult<SearchResult> {
        return try {
            val response = apiService.searchVehicle(
                mapOf("query" to query, "search_type" to searchType, "source" to source)
            )
            if (response.isSuccessful) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error("Vehicle not found", code = response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(message = e.message ?: "Network error")
        }
    }

    override suspend fun getSearchHistory(): NetworkResult<List<SearchHistoryItem>> {
        return try {
            val response = apiService.getSearchHistory()
            if (response.isSuccessful) {
                NetworkResult.Success(response.body()!!.results)
            } else {
                NetworkResult.Error("Failed to load history", code = response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(message = e.message ?: "Network error")
        }
    }

    override suspend fun getSavedVehicles(): NetworkResult<List<Vehicle>> {
        return try {
            val response = apiService.getSavedVehicles()
            if (response.isSuccessful) {
                val vehicles = response.body()!!.results.map { it.toVehicle() }
                NetworkResult.Success(vehicles)
            } else {
                NetworkResult.Error("Failed to load saved vehicles", code = response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(message = e.message ?: "Network error")
        }
    }

    override suspend fun saveVehicle(
        vin: String,
        plateNumber: String,
        name: String,
    ): NetworkResult<Vehicle> {
        return try {
            val response = apiService.saveVehicle(
                mapOf("vin" to vin, "nickname" to name)
            )
            if (response.isSuccessful) {
                NetworkResult.Success(response.body()!!.toVehicle())
            } else {
                NetworkResult.Error("Failed to save vehicle", code = response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(message = e.message ?: "Network error")
        }
    }

    override suspend fun deleteSavedVehicle(id: String): NetworkResult<Unit> {
        return try {
            val response = apiService.deleteSavedVehicle(id)
            if (response.isSuccessful || response.code() == 204) {
                NetworkResult.Success(Unit)
            } else {
                NetworkResult.Error("Failed to delete", code = response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(message = e.message ?: "Network error")
        }
    }

    override suspend fun getAccidentDetail(accidentId: String): NetworkResult<Accident> {
        return try {
            val response = apiService.getAccidentDetail(accidentId)
            if (response.isSuccessful) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error("Accident not found", code = response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(message = e.message ?: "Network error")
        }
    }

    override suspend fun reportAccident(
        plateNumber: String,
        photoUri: Uri,
        description: String,
        severity: String,
        location: String,
    ): NetworkResult<AccidentReportResponse> {
        return try {
            val inputStream = context.contentResolver.openInputStream(photoUri)
                ?: return NetworkResult.Error("Cannot read photo file")
            val bytes = inputStream.readBytes()
            inputStream.close()

            val textType = "text/plain".toMediaTypeOrNull()
            val photoPart = MultipartBody.Part.createFormData(
                "photo",
                "accident_photo.jpg",
                bytes.toRequestBody("image/*".toMediaTypeOrNull()),
            )

            val response = apiService.reportAccident(
                plateNumber = plateNumber.toRequestBody(textType),
                photo = photoPart,
                description = description.toRequestBody(textType),
                severity = severity.toRequestBody(textType),
                location = location.toRequestBody(textType),
            )
            if (response.isSuccessful) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error("Failed to report accident", code = response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(message = e.message ?: "Network error")
        }
    }
}
