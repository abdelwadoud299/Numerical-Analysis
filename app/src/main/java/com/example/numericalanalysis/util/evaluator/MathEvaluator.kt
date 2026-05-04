package com.example.numericalanalysis.util.evaluator

import org.mariuszgromada.math.mxparser.Argument
import org.mariuszgromada.math.mxparser.Expression

/**
 * Utility to evaluate mathematical expressions using mXparser.
 */
object MathEvaluator {
    fun evaluate(expression: String, xValue: Double): Double {
        val x = Argument("x", xValue)
        val e = Expression(expression, x)
        return e.calculate()
    }

    /**
     * Evaluates the expression for a range of values more efficiently by reusing objects.
     */
    fun evaluateRange(expression: String, startX: Double, endX: Double, step: Double): List<Pair<Double, Double>> {
        val x = Argument("x")
        val e = Expression(expression, x)
        val results = mutableListOf<Pair<Double, Double>>()
        
        var currentX = startX
        while (currentX <= endX) {
            x.setArgumentValue(currentX)
            val y = e.calculate()
            if (!y.isNaN()) {
                results.add(currentX to y)
            }
            currentX += step
        }
        return results
    }

    fun isValid(expression: String): Boolean {
        val x = Argument("x", 0.0)
        val e = Expression(expression, x)
        return e.checkSyntax()
    }
}
