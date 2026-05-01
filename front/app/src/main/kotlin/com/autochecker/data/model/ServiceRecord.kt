package com.autochecker.data.model

data class ServiceRecord(
    val id: Int = 0,
    val guid: String = "",
    val date: String = "",
    val description: String = "",
    val location: String = "",
    val cost: String? = null,
) {
    val serviceCenter: String get() = location
}
