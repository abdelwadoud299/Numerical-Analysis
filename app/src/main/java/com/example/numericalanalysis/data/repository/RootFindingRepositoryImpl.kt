package com.example.numericalanalysis.data.repository

import com.example.numericalanalysis.data.model.IterationStep
import com.example.numericalanalysis.util.evaluator.MathEvaluator
import kotlin.math.abs

class RootFindingRepositoryImpl : RootFindingRepository {

    override fun bisection(f: String, a: Double, b: Double, tol: Double, maxIter: Int): List<IterationStep> {
        val steps = mutableListOf<IterationStep>()
        var low = a
        var high = b

        val fLowInit = MathEvaluator.evaluate(f, low)
        val fHighInit = MathEvaluator.evaluate(f, high)

        // Check if boundaries are already roots
        if (abs(fLowInit) < 1e-12) return listOf(IterationStep(1, low, fLowInit, 0.0, low, high))
        if (abs(fHighInit) < 1e-12) return listOf(IterationStep(1, high, fHighInit, 0.0, low, high))

        if (fLowInit * fHighInit > 0) return emptyList()

        for (i in 1..maxIter) {
            val xr = (low + high) / 2.0
            val fxr = MathEvaluator.evaluate(f, xr)
            val fLow = MathEvaluator.evaluate(f, low)
            
            val error = if (steps.isNotEmpty()) abs((xr - steps.last().xr) / xr) else null
            steps.add(IterationStep(i, xr, fxr, error, low, high))

            if (abs(fxr) < 1e-15 || (error != null && error < tol)) break

            if (fLow * fxr < 0) {
                high = xr
            } else {
                low = xr
            }
        }
        return steps
    }

    override fun falsePosition(f: String, a: Double, b: Double, tol: Double, maxIter: Int): List<IterationStep> {
        val steps = mutableListOf<IterationStep>()
        var low = a
        var high = b
        
        if (MathEvaluator.evaluate(f, low) * MathEvaluator.evaluate(f, high) > 0) return emptyList()

        for (i in 1..maxIter) {
            val fLow = MathEvaluator.evaluate(f, low)
            val fHigh = MathEvaluator.evaluate(f, high)
            val xr = high - (fHigh * (low - high)) / (fLow - fHigh)
            val fxr = MathEvaluator.evaluate(f, xr)
            
            val error = if (steps.isNotEmpty()) abs((xr - steps.last().xr) / xr) else null
            steps.add(IterationStep(i, xr, fxr, error, low, high))

            if (abs(fxr) < 1e-15 || (error != null && error < tol)) break

            if (fLow * fxr < 0) {
                high = xr
            } else {
                low = xr
            }
        }
        return steps
    }

    override fun fixedPoint(f: String, g: String, x0: Double, tol: Double, maxIter: Int): List<IterationStep> {
        val steps = mutableListOf<IterationStep>()
        var xr = x0
        
        for (i in 1..maxIter) {
            val nextXr = MathEvaluator.evaluate(g, xr)
            if (nextXr.isNaN()) break
            
            val fxr = MathEvaluator.evaluate(f, nextXr)
            val error = abs((nextXr - xr) / nextXr)
            
            steps.add(IterationStep(i, nextXr, fxr, error, a = xr)) // a is xi
            xr = nextXr
            
            if (error < tol) break
        }
        return steps
    }

    override fun newtonRaphson(f: String, df: String, x0: Double, tol: Double, maxIter: Int): List<IterationStep> {
        val steps = mutableListOf<IterationStep>()
        var xr = x0

        for (i in 1..maxIter) {
            val fx = MathEvaluator.evaluate(f, xr)
            val dfx = MathEvaluator.evaluate(df, xr) // Evaluator should handle derivative
            
            if (abs(dfx) < 1e-15) break
            
            val nextXr = xr - (fx / dfx)
            val error = abs((nextXr - xr) / nextXr)
            
            steps.add(IterationStep(i, nextXr, MathEvaluator.evaluate(f, nextXr), error, a = xr)) // a is xi
            xr = nextXr
            
            if (error < tol) break
        }
        return steps
    }

    override fun secant(f: String, x0: Double, x1: Double, tol: Double, maxIter: Int): List<IterationStep> {
        val steps = mutableListOf<IterationStep>()
        var xPrev = x0
        var xCurr = x1

        for (i in 1..maxIter) {
            val fPrev = MathEvaluator.evaluate(f, xPrev)
            val fCurr = MathEvaluator.evaluate(f, xCurr)
            
            if (abs(fCurr - fPrev) < 1e-15) break
            
            val xNext = xCurr - (fCurr * (xPrev - xCurr)) / (fPrev - fCurr)
            val error = abs((xNext - xCurr) / xNext)
            
            steps.add(IterationStep(i, xNext, MathEvaluator.evaluate(f, xNext), error, a = xPrev, b = xCurr)) // a=xi-1, b=xi, xr=xi+1
            
            xPrev = xCurr
            xCurr = xNext
            
            if (error < tol) break
        }
        return steps
    }
}
