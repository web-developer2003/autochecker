package com.autochecker.data.model

data class LegalStatus(
    val id: Int = 0,
    val guid: String = "",
    val liens: String = "",
    val liensStatus: String = "clean",
    val restrictions: String = "",
    val restrictionsStatus: String = "clean",
    val theftRecords: String = "",
    val theftStatus: String = "clean",
    val registration: String = "",
    val registrationStatus: String = "clean",
) {
    val isStolen: Boolean get() = theftStatus == "issue"
    val hasPledge: Boolean get() = liensStatus == "issue"
    val hasRestrictions: Boolean get() = restrictionsStatus == "issue"
    val hasArrest: Boolean get() = false
    val pledgeDetails: String? get() = liens.takeIf { it.isNotBlank() }
    val restrictionDetails: String? get() = restrictions.takeIf { it.isNotBlank() }
}
