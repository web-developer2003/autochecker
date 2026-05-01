package com.autochecker.data.model

data class AccidentReportResponse(
    val message: String = "",
    val messageRu: String = "",
    val messageEn: String = "",
    val accidentGuid: String = "",
    val vehicleGuid: String = "",
    val vehicleCreated: Boolean = false,
)
