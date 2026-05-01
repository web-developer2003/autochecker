package com.autochecker.data.model

data class User(
    val id: Int = 0,
    val guid: String = "",
    val username: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val photo: String? = null,
    val role: Int = 0,
) {
    val fullName: String get() = "$firstName $lastName".trim()
    val avatarUrl: String? get() = photo
}
