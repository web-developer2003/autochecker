package com.autochecker.data.model

data class SearchHistoryItem(
    val id: Int = 0,
    val guid: String = "",
    val vehicle: VehicleShort = VehicleShort(),
    val vehicleCheck: Int? = null,
    val createdAt: String = "",
) {
    val vin: String get() = vehicle.vin
    val searchedAt: String get() = createdAt
}

data class VehicleShort(
    val id: Int = 0,
    val guid: String = "",
    val make: String = "",
    val model: String = "",
    val year: Int = 0,
    val vin: String = "",
    val plate: String = "",
    val displayName: String = "",
) {
    fun toVehicle(): Vehicle = Vehicle(
        id = id,
        guid = guid,
        vin = vin,
        plate = plate,
        make = make,
        model = model,
        year = year,
    )
}
