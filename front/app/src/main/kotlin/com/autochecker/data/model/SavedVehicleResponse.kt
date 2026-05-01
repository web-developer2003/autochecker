package com.autochecker.data.model

data class SavedVehicleResponse(
    val id: Int = 0,
    val guid: String = "",
    val vehicle: VehicleShort = VehicleShort(),
    val nickname: String = "",
    val createdAt: String = "",
) {
    fun toVehicle(): Vehicle = Vehicle(
        id = vehicle.id,
        guid = vehicle.guid,
        vin = vehicle.vin,
        plate = vehicle.plate,
        make = vehicle.make,
        model = vehicle.model,
        year = vehicle.year,
        isSaved = true,
    )
}
