package com.autochecker.data.model

data class MileageRecord(
    val id: Int = 0,
    val guid: String = "",
    val date: String = "",
    val mileageKm: Int = 0,
    val source: String = "",
) {
    val mileage: Int get() = mileageKm
}
