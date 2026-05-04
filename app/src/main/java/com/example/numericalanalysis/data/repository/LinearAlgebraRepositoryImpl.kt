package com.example.numericalanalysis.data.repository

import com.example.numericalanalysis.data.model.MatrixStep
import kotlin.math.abs

class LinearAlgebraRepositoryImpl : LinearAlgebraRepository {

    override fun gaussElimination(matrix: Array<DoubleArray>, constants: DoubleArray): List<MatrixStep> {
        val steps = mutableListOf<MatrixStep>()
        val n = constants.size
        val a = matrix.map { it.copyOf() }.toTypedArray()
        val b = constants.copyOf()

        steps.add(MatrixStep("Initial Augmented Matrix", a.map { it.copyOf() }.toTypedArray(), b.copyOf()))

        for (i in 0 until n) {
            var maxRow = i
            for (k in i + 1 until n) {
                if (abs(a[k][i]) > abs(a[maxRow][i])) maxRow = k
            }
            if (maxRow != i) {
                val tempA = a[i]; a[i] = a[maxRow]; a[maxRow] = tempA
                val tempB = b[i]; b[i] = b[maxRow]; b[maxRow] = tempB
                steps.add(MatrixStep("Pivoting: Swapped R${i+1} with R${maxRow+1}", a.map { it.copyOf() }.toTypedArray(), b.copyOf()))
            }

            for (k in i + 1 until n) {
                if (abs(a[i][i]) < 1e-15) continue
                val factor = a[k][i] / a[i][i]
                b[k] -= factor * b[i]
                for (j in i until n) a[k][j] -= factor * a[i][j]
                steps.add(MatrixStep("R${k+1} = R${k+1} - (${"%.2f".format(factor)}) * R${i+1}", a.map { it.copyOf() }.toTypedArray(), b.copyOf()))
            }
        }

        val x = DoubleArray(n)
        for (i in n - 1 downTo 0) {
            var sum = 0.0
            for (j in i + 1 until n) sum += a[i][j] * x[j]
            x[i] = (b[i] - sum) / a[i][i]
        }
        steps.add(MatrixStep("Final Solution via Back Substitution", a, x))
        return steps
    }

    override fun luDecomposition(matrix: Array<DoubleArray>, constants: DoubleArray): List<MatrixStep> {
        val n = constants.size
        val l = Array(n) { DoubleArray(n) { if (it == it) 0.0 else 0.0 } }
        val u = Array(n) { DoubleArray(n) }
        val steps = mutableListOf<MatrixStep>()

        for (i in 0 until n) l[i][i] = 1.0

        for (i in 0 until n) {
            for (j in i until n) {
                var sum = 0.0
                for (k in 0 until i) sum += l[i][k] * u[k][j]
                u[i][j] = matrix[i][j] - sum
            }
            steps.add(MatrixStep("Updated U Matrix (Row $i)", u.map { it.copyOf() }.toTypedArray()))
            for (j in i + 1 until n) {
                var sum = 0.0
                for (k in 0 until i) sum += l[j][k] * u[k][i]
                l[j][i] = (matrix[j][i] - sum) / u[i][i]
            }
            steps.add(MatrixStep("Updated L Matrix (Col $i)", l.map { it.copyOf() }.toTypedArray()))
        }
        return steps
    }

    override fun cramersRule(matrix: Array<DoubleArray>, constants: DoubleArray): List<MatrixStep> {
        val n = constants.size
        val steps = mutableListOf<MatrixStep>()
        val detMain = determinant(matrix)
        steps.add(MatrixStep("Main Determinant D = %.4f".format(detMain), matrix.map { it.copyOf() }.toTypedArray()))

        val x = DoubleArray(n)
        for (i in 0 until n) {
            val ai = matrix.map { it.copyOf() }.toTypedArray()
            for (j in 0 until n) ai[j][i] = constants[j]
            val detAi = determinant(ai)
            x[i] = detAi / detMain
            steps.add(MatrixStep("D${i+1} = %.4f (Column ${i+1} replaced)".format(detAi), ai))
        }
        steps.add(MatrixStep("Final Solution (x_i = Di / D)", matrix, x))
        return steps
    }

    override fun gaussJordan(matrix: Array<DoubleArray>, constants: DoubleArray): List<MatrixStep> {
        val n = constants.size
        val a = matrix.map { it.copyOf() }.toTypedArray()
        val b = constants.copyOf()
        val steps = mutableListOf<MatrixStep>()

        for (i in 0 until n) {
            val div = a[i][i]
            b[i] /= div
            for (j in 0 until n) a[i][j] /= div
            steps.add(MatrixStep("Normalize R${i+1}: Divide by %.2f".format(div), a.map { it.copyOf() }.toTypedArray(), b.copyOf()))

            for (k in 0 until n) {
                if (k != i) {
                    val factor = a[k][i]
                    b[k] -= factor * b[i]
                    for (j in 0 until n) a[k][j] -= factor * a[i][j]
                    steps.add(MatrixStep("Eliminate Col ${i+1} in R${k+1}", a.map { it.copyOf() }.toTypedArray(), b.copyOf()))
                }
            }
        }
        return steps
    }

    private fun determinant(matrix: Array<DoubleArray>): Double {
        val n = matrix.size
        if (n == 1) return matrix[0][0]
        if (n == 2) return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]
        var res = 0.0
        for (j in 0 until n) {
            res += (if (j % 2 == 0) 1 else -1) * matrix[0][j] * determinant(minor(matrix, 0, j))
        }
        return res
    }

    private fun minor(matrix: Array<DoubleArray>, row: Int, col: Int): Array<DoubleArray> {
        val n = matrix.size
        return Array(n - 1) { i ->
            val r = if (i < row) i else i + 1
            DoubleArray(n - 1) { j ->
                val c = if (j < col) j else j + 1
                matrix[r][c]
            }
        }
    }
}
