package com.example.numericalanalysis.data.model

/**
 * Represents a step in a linear algebra algorithm (e.g., Gauss Elimination).
 */
data class MatrixStep(
    val stepDescription: String,
    val matrix: Array<DoubleArray>,
    val b: DoubleArray? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MatrixStep

        if (stepDescription != other.stepDescription) return false
        if (!matrix.contentDeepEquals(other.matrix)) return false
        if (b != null) {
            if (other.b == null) return false
            if (!b.contentEquals(other.b)) return false
        } else if (other.b != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = stepDescription.hashCode()
        result = 31 * result + matrix.contentDeepHashCode()
        result = 31 * result + (b?.contentHashCode() ?: 0)
        return result
    }
}
