package com.autochecker.data.repository.impl

import com.autochecker.core.network.ApiService
import com.autochecker.core.network.NetworkResult
import com.autochecker.data.model.User
import com.autochecker.data.repository.UserRepository
import javax.inject.Inject

class RealUserRepository @Inject constructor(
    private val apiService: ApiService,
) : UserRepository {

    override suspend fun getProfile(): NetworkResult<User> {
        return try {
            val response = apiService.getProfile()
            if (response.isSuccessful) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error("Failed to load profile", code = response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(message = e.message ?: "Network error")
        }
    }

    override suspend fun updateProfile(
        fullName: String,
        email: String,
        phone: String,
    ): NetworkResult<User> {
        return try {
            val parts = fullName.trim().split(" ", limit = 2)
            val firstName = parts.firstOrNull() ?: ""
            val lastName = if (parts.size > 1) parts[1] else ""

            val response = apiService.updateProfile(
                mapOf(
                    "first_name" to firstName,
                    "last_name" to lastName,
                    "email" to email,
                    "phone" to phone,
                )
            )
            if (response.isSuccessful) {
                NetworkResult.Success(response.body()!!)
            } else {
                NetworkResult.Error("Failed to update profile", code = response.code())
            }
        } catch (e: Exception) {
            NetworkResult.Error(message = e.message ?: "Network error")
        }
    }
}
