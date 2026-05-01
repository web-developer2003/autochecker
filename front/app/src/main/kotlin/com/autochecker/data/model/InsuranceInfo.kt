package com.autochecker.data.model

data class InsuranceInfo(
    val provider: String = "",
    val policyNumber: String = "",
    val validFrom: String = "",
    val validUntil: String = "",
    val type: String = "",
)
