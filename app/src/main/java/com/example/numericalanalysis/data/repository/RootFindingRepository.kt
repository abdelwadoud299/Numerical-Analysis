package com.example.numericalanalysis.data.repository

import com.example.numericalanalysis.data.model.IterationStep

/**
 * Interface for Root-Finding numerical methods.
 */
interface RootFindingRepository {
    fun bisection(f: String, a: Double, b: Double, tol: Double, maxIter: Int): List<IterationStep>
    fun falsePosition(f: String, a: Double, b: Double, tol: Double, maxIter: Int): List<IterationStep>
    fun fixedPoint(f: String, g: String, x0: Double, tol: Double, maxIter: Int): List<IterationStep>
    fun newtonRaphson(f: String, df: String, x0: Double, tol: Double, maxIter: Int): List<IterationStep>
    fun secant(f: String, x0: Double, x1: Double, tol: Double, maxIter: Int): List<IterationStep>
}
