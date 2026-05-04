package com.example.numericalanalysis.ui.viewmodel

import com.example.numericalanalysis.data.model.MatrixStep
import kotlinx.coroutines.flow.StateFlow

/**
 * Interface defining the base structure for Linear Algebra ViewModels.
 */
interface LinearAlgebraViewModel {
    val matrixSize: StateFlow<Int>
    val matrix: StateFlow<Array<DoubleArray>>
    val constants: StateFlow<DoubleArray>
    val steps: StateFlow<List<MatrixStep>>
    val solutions: StateFlow<DoubleArray?>
    val isProcessing: StateFlow<Boolean>
    val error: StateFlow<String?>
    val executionTimeMs: StateFlow<Long>

    fun onSizeChanged(newSize: Int)
    fun updateMatrixElement(row: Int, col: Int, value: Double)
    fun updateConstantElement(index: Int, value: Double)
    
    fun solveGauss()
    fun solveLU()
    fun solveCramer()
    fun solveGaussJordan()
}
