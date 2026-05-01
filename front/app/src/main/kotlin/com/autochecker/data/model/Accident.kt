package com.autochecker.data.model

import com.google.gson.annotations.SerializedName

data class Accident(
    val id: Int = 0,
    val guid: String = "",
    val date: String = "",
    val severity: AccidentSeverity? = null,
    val damageArea: String = "",
    val damageDescription: String? = null,
    val repairCost: String = "",
    val accidentType: String = "",
    val location: String = "",
    val impactZone: String = "",
    val airbagsDeployed: Boolean = false,
    val repairStatus: String = "",
    val policeReportNumber: String = "",
    val damageAreas: List<String>? = null,
    val photos: List<AccidentPhoto>? = null,
    val insuranceClaim: InsuranceClaim? = null,
    val thumbnail: String? = null,
) {
    val description: String get() = damageDescription ?: ""
    val imageUrls: List<String> get() = (photos ?: emptyList()).mapNotNull { it.photoUrl }
}

data class AccidentPhoto(
    val id: Int = 0,
    val guid: String = "",
    val photoUrl: String? = null,
    val description: String = "",
)

data class InsuranceClaim(
    val id: Int = 0,
    val guid: String = "",
    val claimStatus: String = "",
    val provider: String = "",
    val claimNumber: String = "",
    val payoutAmount: String? = null,
)

enum class AccidentSeverity {
    @SerializedName("minor") MINOR,
    @SerializedName("moderate") MODERATE,
    @SerializedName("major") MAJOR,
}
