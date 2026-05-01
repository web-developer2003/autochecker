package com.autochecker.data.model

data class VehicleReport(
    val check: VehicleCheck = VehicleCheck(),
    val accidentSummary: AccidentSummary = AccidentSummary(),
    val accidents: List<Accident> = emptyList(),
    val mileageRecords: List<MileageRecord> = emptyList(),
    val legalStatus: LegalStatus? = null,
    val serviceRecords: List<ServiceRecord> = emptyList(),
) {
    val vehicle: Vehicle get() = check.vehicle

    val overallStatus: ReportStatus
        get() = when {
            check.accidentsStatus == "warning" || check.legalStatus == "issues_found" -> ReportStatus.WARNING
            check.accidentsStatus == "issues_found" -> ReportStatus.WARNING
            else -> ReportStatus.CLEAN
        }

    val accidentCount: Int get() = accidentSummary.totalAccidents
    val ownerCount: Int get() = 0
    val checkedAt: String get() = check.createdAt
}

enum class ReportStatus {
    CLEAN,
    WARNING,
    CRITICAL,
}
