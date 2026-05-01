package com.autochecker.data.mock

import com.autochecker.core.network.NetworkResult
import com.autochecker.data.model.User
import com.autochecker.data.repository.UserRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockUserRepository @Inject constructor() : UserRepository {
    private var currentUser = MockData.user

    override suspend fun getProfile(): NetworkResult<User> {
        delay(500)
        return NetworkResult.Success(currentUser)
    }

    override suspend fun updateProfile(fullName: String, email: String, phone: String): NetworkResult<User> {
        delay(800)
        val parts = fullName.trim().split(" ", limit = 2)
        currentUser = currentUser.copy(
            firstName = parts.firstOrNull() ?: "",
            lastName = if (parts.size > 1) parts[1] else "",
            email = email,
            phone = phone,
        )
        return NetworkResult.Success(currentUser)
    }
}
