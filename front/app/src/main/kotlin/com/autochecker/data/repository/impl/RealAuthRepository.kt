package com.autochecker.data.repository.impl

import com.autochecker.core.network.ApiService
import com.autochecker.core.network.NetworkResult
import com.autochecker.data.local.TokenManager
import com.autochecker.data.model.User
import com.autochecker.data.repository.AuthRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class RealAuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager,
) : AuthRepository {

    override suspend fun login(email: String, password: String): NetworkResult<User> {
        return try {
            val response = apiService.login(
                mapOf("username" to email, "password" to password)
            )
            if (response.isSuccessful) {
                val body = response.body()!!
                tokenManager.saveTokens(body.access, body.refresh)
                val user = User(
                    id = body.userId,
                    guid = body.userGuid,
                    username = body.username,
                    role = body.role,
                )
                NetworkResult.Success(user)
            } else {
                NetworkResult.Error(
                    message = "Login failed",
                    code = response.code(),
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(message = e.message ?: "Network error")
        }
    }

    override suspend fun register(
        fullName: String,
        email: String,
        phone: String,
        password: String,
    ): NetworkResult<User> {
        return try {
            val parts = fullName.trim().split(" ", limit = 2)
            val firstName = parts.firstOrNull() ?: ""
            val lastName = if (parts.size > 1) parts[1] else ""

            val response = apiService.register(
                mapOf(
                    "first_name" to firstName,
                    "last_name" to lastName,
                    "email" to email,
                    "password" to password,
                    "password_confirm" to password,
                )
            )
            if (response.isSuccessful) {
                val body = response.body()!!
                tokenManager.saveTokens(body.access, body.refresh)
                val user = User(
                    id = body.id,
                    guid = body.guid,
                    firstName = body.firstName,
                    lastName = body.lastName,
                    email = body.email,
                )
                NetworkResult.Success(user)
            } else {
                NetworkResult.Error(
                    message = "Registration failed",
                    code = response.code(),
                )
            }
        } catch (e: Exception) {
            NetworkResult.Error(message = e.message ?: "Network error")
        }
    }

    override suspend fun logout() {
        try {
            val refresh = tokenManager.refreshToken.first()
            if (refresh != null) {
                apiService.logout(mapOf("refresh" to refresh))
            }
        } catch (_: Exception) {
        }
        tokenManager.clearToken()
    }

    override suspend fun isLoggedIn(): Boolean {
        return tokenManager.token.first() != null
    }
}
