package com.autochecker.data.model

import com.google.gson.annotations.SerializedName

data class Accident(
    val id: Int = 0,
    val guid: String? = null,
    val date: String? = null,
    val severity: AccidentSeverity? = null,
    val damageArea: String? = null,
    val damageDescription: String? = null,
    val repairCost: String? = null,
    val accidentType: String? = null,
    val location: String? = null,
    val impactZone: String? = null,
    val airbagsDeployed: Boolean? = null,
    val repairStatus: String? = null,
    val policeReportNumber: String? = null,
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
