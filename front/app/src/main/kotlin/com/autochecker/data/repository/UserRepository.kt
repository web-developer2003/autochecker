package com.autochecker.data.repository

import com.autochecker.core.network.NetworkResult
import com.autochecker.data.model.User

interface UserRepository {
    suspend fun getProfile(): NetworkResult<User>
    suspend fun updateProfile(fullName: String, email: String, phone: String): NetworkResult<User>
}
