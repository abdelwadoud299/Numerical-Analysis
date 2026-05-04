package com.example.numericalanalysis.data.model

/**
 * A generic wrapper for calculation results.
 */
sealed class CalculationResult<out T> {
    data class Success<out T>(val data: T, val executionTimeMs: Long) : CalculationResult<T>()
    data class Error(val message: String) : CalculationResult<Nothing>()
    object Loading : CalculationResult<Nothing>()
}
