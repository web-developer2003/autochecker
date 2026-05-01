package com.autochecker.data.model

data class SearchResult(
    val vehicle: Vehicle = Vehicle(),
    val checkGuid: String? = null,
    val source: String = "local",
    val internetSummary: String? = null,
    val message: String? = null,
)
