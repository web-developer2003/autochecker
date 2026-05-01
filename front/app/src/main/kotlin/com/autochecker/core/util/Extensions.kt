package com.autochecker.core.util

import java.text.NumberFormat
import java.util.Locale

fun Int.formatWithSpaces(): String {
    return NumberFormat.getInstance(Locale("uz")).format(this)
}

fun String.capitalizeFirst(): String {
    return replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}
