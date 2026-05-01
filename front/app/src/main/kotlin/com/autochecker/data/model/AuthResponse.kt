package com.autochecker.data.model

data class LoginResponse(
    val userId: Int = 0,
    val userGuid: String = "",
    val username: String = "",
    val role: Int = 0,
    val access: String = "",
    val refresh: String = "",
)

data class RegisterResponse(
    val id: Int = 0,
    val guid: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val access: String = "",
    val refresh: String = "",
)
