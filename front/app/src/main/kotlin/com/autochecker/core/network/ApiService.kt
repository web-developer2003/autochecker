package com.autochecker.core.network

import com.autochecker.data.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Auth
    @POST("auth/login/")
    suspend fun login(@Body body: Map<String, String>): Response<LoginResponse>

    @POST("auth/register/")
    suspend fun register(@Body body: Map<String, String>): Response<RegisterResponse>

    @POST("auth/logout/")
    suspend fun logout(@Body body: Map<String, String>): Response<Unit>

    // Vehicle search (returns vehicle + check_guid)
    @POST("vehicle/search/")
    suspend fun searchVehicle(@Body body: Map<String, String>): Response<SearchResult>

    // Full report
    @GET("check/{guid}/full-report/")
    suspend fun getFullReport(@Path("guid") guid: String): Response<VehicleReport>

    // History (paginated)
    @GET("history/")
    suspend fun getSearchHistory(@Query("p") page: Int = 1): Response<PaginatedResponse<SearchHistoryItem>>

    // Accident detail
    @GET("accident/{guid}/")
    suspend fun getAccidentDetail(@Path("guid") guid: String): Response<Accident>

    // Report accident with photo
    @Multipart
    @POST("accident/report/")
    suspend fun reportAccident(
        @Part("plate_number") plateNumber: RequestBody,
        @Part photo: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("severity") severity: RequestBody,
        @Part("location") location: RequestBody,
    ): Response<AccidentReportResponse>

    // Profile
    @GET("profile/")
    suspend fun getProfile(): Response<User>

    @PUT("profile/")
    suspend fun updateProfile(@Body body: Map<String, String>): Response<User>

    // Saved Vehicles (paginated)
    @GET("saved-vehicle/")
    suspend fun getSavedVehicles(@Query("p") page: Int = 1): Response<PaginatedResponse<SavedVehicleResponse>>

    @POST("saved-vehicle/")
    suspend fun saveVehicle(@Body body: Map<String, String>): Response<SavedVehicleResponse>

    @DELETE("saved-vehicle/{guid}/")
    suspend fun deleteSavedVehicle(@Path("guid") guid: String): Response<Unit>
}
