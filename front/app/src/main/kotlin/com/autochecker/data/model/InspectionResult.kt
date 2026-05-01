package com.autochecker.data.model

data class InspectionResult(
    val date: String = "",
    val result: String = "",
    val validUntil: String = "",
    val station: String = "",
)
