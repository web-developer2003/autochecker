package com.autochecker.data.repository

import com.autochecker.core.network.NetworkResult
import com.autochecker.data.model.User

interface AuthRepository {
    suspend fun login(email: String, password: String): NetworkResult<User>
    suspend fun register(fullName: String, email: String, phone: String, password: String): NetworkResult<User>
    suspend fun logout()
    suspend fun isLoggedIn(): Boolean
}
