package com.autochecker.data.model

data class Vehicle(
    val id: Int = 0,
    val guid: String = "",
    val vin: String = "",
    val plate: String = "",
    val make: String = "",
    val model: String = "",
    val year: Int = 0,
    val color: String = "",
    val bodyType: String = "",
    val engineType: String = "",
    val engineVolume: String = "",
    val transmission: String = "",
    val driveType: String = "",
    val imageUrl: String? = null,
    val isSaved: Boolean = false,
) {
    val plateNumber: String get() = plate
}
