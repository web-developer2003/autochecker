package com.autochecker.data.model

data class VehicleCheck(
    val id: Int = 0,
    val guid: String = "",
    val vehicle: Vehicle = Vehicle(),
    val accidentsCount: Int = 0,
    val accidentsStatus: String = "clean",
    val mileageStatus: String = "clean",
    val legalStatus: String = "clean",
    val serviceRecordsCount: Int = 0,
    val taxiUsage: Boolean = false,
    val createdAt: String = "",
)
