package com.example.numericalanalysis.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.numericalanalysis.data.model.IterationStep
import com.example.numericalanalysis.data.repository.RootFindingRepository
import com.example.numericalanalysis.data.repository.RootFindingRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class RootFindingViewModelImpl(
    private val repository: RootFindingRepository = RootFindingRepositoryImpl()
) : ViewModel(), RootFindingViewModel {

    private val _equation = MutableStateFlow("x^2 - 4")
    override val equation: StateFlow<String> = _equation

    private val _gEquation = MutableStateFlow("sqrt(x + 2)")
    override val gEquation: StateFlow<String> = _gEquation

    private val _steps = MutableStateFlow<List<IterationStep>>(emptyList())
    override val steps: StateFlow<List<IterationStep>> = _steps

    private val _result = MutableStateFlow<Double?>(null)
    override val result: StateFlow<Double?> = _result

    private val _error = MutableStateFlow<String?>(null)
    override val error: StateFlow<String?> = _error

    private val _isProcessing = MutableStateFlow(false)
    override val isProcessing: StateFlow<Boolean> = _isProcessing

    private val _executionTimeMs = MutableStateFlow(0L)
    override val executionTimeMs: StateFlow<Long> = _executionTimeMs

    override fun onEquationChanged(newEquation: String) {
        _equation.value = newEquation
    }

    override fun onGEquationChanged(newEquation: String) {
        _gEquation.value = newEquation
    }

    override fun calculate(
        method: String,
        a: Double?,
        b: Double?,
        x0: Double?,
        x1: Double?,
        tolerance: Double,
        maxIterations: Int
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            _isProcessing.value = true
            _error.value = null
            _steps.value = emptyList()
            _result.value = null
            _executionTimeMs.value = 0L

            try {
                val f = _equation.value
                val g = _gEquation.value
                var resultSteps: List<IterationStep> = emptyList()

                val time = measureTimeMillis {
                    resultSteps = when (method) {
                        "Bisection" -> repository.bisection(f, a ?: 0.0, b ?: 0.0, tolerance, maxIterations)
                        "False Position" -> repository.falsePosition(f, a ?: 0.0, b ?: 0.0, tolerance, maxIterations)
                        "Fixed Point" -> repository.fixedPoint(f, g, x0 ?: 0.0, tolerance, maxIterations)
                        "Newton-Raphson" -> repository.newtonRaphson(f, "der($f, x, x)", x0 ?: 0.0, tolerance, maxIterations)
                        "Secant" -> repository.secant(f, a ?: 0.0, b ?: 0.0, tolerance, maxIterations)
                        else -> emptyList()
                    }
                }

                if (resultSteps.isEmpty()) {
                    _error.value = "Root not found. Check brackets or initial guess."
                } else {
                    _steps.value = resultSteps
                    _result.value = resultSteps.last().xr
                    _executionTimeMs.value = time
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An unknown error occurred"
            } finally {
                _isProcessing.value = false
            }
        }
    }
}
