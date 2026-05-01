package com.autochecker.data.mock

import com.autochecker.core.network.NetworkResult
import com.autochecker.data.local.TokenManager
import com.autochecker.data.model.User
import com.autochecker.data.repository.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class MockAuthRepository @Inject constructor(
    private val tokenManager: TokenManager,
) : AuthRepository {
    override suspend fun login(email: String, password: String): NetworkResult<User> {
        delay(800)
        return if (email.isNotBlank() && password.length >= 6) {
            tokenManager.saveToken("mock_token_${System.currentTimeMillis()}")
            NetworkResult.Success(MockData.user.copy(email = email))
        } else {
            NetworkResult.Error("Email yoki parol noto'g'ri")
        }
    }

    override suspend fun register(
        fullName: String, email: String, phone: String, password: String
    ): NetworkResult<User> {
        delay(1000)
        val parts = fullName.trim().split(" ", limit = 2)
        return if (email.isNotBlank() && password.length >= 6 && fullName.isNotBlank()) {
            tokenManager.saveToken("mock_token_${System.currentTimeMillis()}")
            NetworkResult.Success(
                MockData.user.copy(
                    firstName = parts.firstOrNull() ?: "",
                    lastName = if (parts.size > 1) parts[1] else "",
                    email = email,
                    phone = phone,
                )
            )
        } else {
            NetworkResult.Error("Ma'lumotlarni to'ldiring")
        }
    }

    override suspend fun logout() {
        tokenManager.clearToken()
    }

    override suspend fun isLoggedIn(): Boolean {
        return tokenManager.token.first() != null
    }
}
