package com.autochecker.data.model

data class AccidentSummary(
    val totalAccidents: Int = 0,
    val majorCount: Int = 0,
    val minorCount: Int = 0,
    val moderateCount: Int = 0,
    val totalDamageCost: String = "0",
)
