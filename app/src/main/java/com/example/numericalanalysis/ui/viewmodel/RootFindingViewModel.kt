package com.example.numericalanalysis.ui.viewmodel

import com.example.numericalanalysis.data.model.IterationStep
import kotlinx.coroutines.flow.StateFlow

interface RootFindingViewModel {
    val equation: StateFlow<String>
    val gEquation: StateFlow<String>
    val steps: StateFlow<List<IterationStep>>
    val result: StateFlow<Double?>
    val error: StateFlow<String?>
    val isProcessing: StateFlow<Boolean>
    val executionTimeMs: StateFlow<Long>

    fun onEquationChanged(newEquation: String)
    fun onGEquationChanged(newEquation: String)
    fun calculate(
        method: String,
        a: Double? = null,
        b: Double? = null,
        x0: Double? = null,
        x1: Double? = null,
        tolerance: Double,
        maxIterations: Int
    )
}
