package com.example.numericalanalysis.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.numericalanalysis.data.model.MatrixStep
import com.example.numericalanalysis.data.repository.LinearAlgebraRepository
import com.example.numericalanalysis.data.repository.LinearAlgebraRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class LinearAlgebraViewModelImpl(
    private val repository: LinearAlgebraRepository = LinearAlgebraRepositoryImpl()
) : ViewModel(), LinearAlgebraViewModel {

    private val _matrixSize = MutableStateFlow(3)
    override val matrixSize: StateFlow<Int> = _matrixSize

    private val _matrix = MutableStateFlow(Array(3) { DoubleArray(3) })
    override val matrix: StateFlow<Array<DoubleArray>> = _matrix

    private val _constants = MutableStateFlow(DoubleArray(3))
    override val constants: StateFlow<DoubleArray> = _constants

    private val _steps = MutableStateFlow<List<MatrixStep>>(emptyList())
    override val steps: StateFlow<List<MatrixStep>> = _steps

    private val _solutions = MutableStateFlow<DoubleArray?>(null)
    override val solutions: StateFlow<DoubleArray?> = _solutions

    private val _isProcessing = MutableStateFlow(false)
    override val isProcessing: StateFlow<Boolean> = _isProcessing

    private val _error = MutableStateFlow<String?>(null)
    override val error: StateFlow<String?> = _error

    private val _executionTimeMs = MutableStateFlow(0L)
    override val executionTimeMs: StateFlow<Long> = _executionTimeMs

    override fun onSizeChanged(newSize: Int) {
        _matrixSize.value = newSize
        _matrix.value = Array(newSize) { DoubleArray(newSize) }
        _constants.value = DoubleArray(newSize)
    }

    override fun updateMatrixElement(row: Int, col: Int, value: Double) {
        val currentMatrix = _matrix.value.map { it.copyOf() }.toTypedArray()
        currentMatrix[row][col] = value
        _matrix.value = currentMatrix
    }

    override fun updateConstantElement(index: Int, value: Double) {
        val currentConstants = _constants.value.copyOf()
        currentConstants[index] = value
        _constants.value = currentConstants
    }

    override fun solveGauss() {
        runCalculation { repository.gaussElimination(_matrix.value, _constants.value) }
    }

    override fun solveLU() {
        runCalculation { repository.luDecomposition(_matrix.value, _constants.value) }
    }

    override fun solveCramer() {
        runCalculation { repository.cramersRule(_matrix.value, _constants.value) }
    }

    override fun solveGaussJordan() {
        runCalculation { repository.gaussJordan(_matrix.value, _constants.value) }
    }

    private fun runCalculation(block: () -> List<MatrixStep>) {
        viewModelScope.launch(Dispatchers.Default) {
            _isProcessing.value = true
            _error.value = null
            _executionTimeMs.value = 0L
            try {
                val res: List<MatrixStep>
                val time = measureTimeMillis {
                    res = block()
                }
                _steps.value = res
                _executionTimeMs.value = time
                if (res.isNotEmpty() && res.last().b != null) {
                    _solutions.value = res.last().b
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Calculation error"
            } finally {
                _isProcessing.value = false
            }
        }
    }
}
