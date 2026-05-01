package com.autochecker.core.util

object VinValidator {
    fun isValid(vin: String): Boolean {
        if (vin.length != Constants.VIN_LENGTH) return false
        val pattern = Regex("^[A-HJ-NPR-Z0-9]{17}$")
        return pattern.matches(vin.uppercase())
    }
}
