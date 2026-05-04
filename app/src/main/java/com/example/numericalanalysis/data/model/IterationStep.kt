package com.example.numericalanalysis.data.model

/**
 * Represents a single iteration in root-finding algorithms.
 */
data class IterationStep(
    val iteration: Int,
    val xr: Double,
    val fxr: Double,
    val error: Double? = null,
    val a: Double? = null,
    val b: Double? = null,
    val extraData: Map<String, Double> = emptyMap()
)
