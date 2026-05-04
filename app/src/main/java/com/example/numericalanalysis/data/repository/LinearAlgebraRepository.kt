package com.example.numericalanalysis.data.repository

import com.example.numericalanalysis.data.model.MatrixStep

/**
 * Interface for Linear Algebra numerical methods.
 */
interface LinearAlgebraRepository {
    fun gaussElimination(matrix: Array<DoubleArray>, constants: DoubleArray): List<MatrixStep>
    fun luDecomposition(matrix: Array<DoubleArray>, constants: DoubleArray): List<MatrixStep>
    fun cramersRule(matrix: Array<DoubleArray>, constants: DoubleArray): List<MatrixStep>
    fun gaussJordan(matrix: Array<DoubleArray>, constants: DoubleArray): List<MatrixStep>
}
