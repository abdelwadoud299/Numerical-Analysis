package com.example.numericalanalysis.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import android.graphics.Paint
import android.graphics.Typeface
import com.example.numericalanalysis.data.model.IterationStep
import com.example.numericalanalysis.util.evaluator.MathEvaluator
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

@Composable
fun GraphView(
    equation: String,
    modifier: Modifier = Modifier,
    steps: List<IterationStep> = emptyList(),
    selectedMethod: String = ""
) {
    var scale by remember { mutableFloatStateOf(50f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale = (scale * zoomChange).coerceIn(10f, 500f)
        offset += offsetChange
    }

    // Pre-calculate points only when equation changes or range significantly changes
    // To keep it simple but fast, we use a derived state for the "points"
    // but we'll optimize the drawing by only calculating what's needed.
    
    Canvas(
        modifier = modifier
            .fillMaxSize()
            .transformable(state = state)
    ) {
        val width = size.width
        val height = size.height
        val centerX = width / 2 + offset.x
        val centerY = height / 2 + offset.y

        // --- Draw Grid ---
        val gridSpacing = scale // 1 unit in math = 'scale' pixels
        val leftEdge = -centerX / scale
        val rightEdge = (width - centerX) / scale
        val topEdge = centerY / scale
        val bottomEdge = (centerY - height) / scale

        // Vertical lines & numbers
        for (x in floor(leftEdge).toInt()..ceil(rightEdge).toInt()) {
            val canvasX = centerX + x * scale
            drawLine(
                color = if (x == 0) Color.Gray else Color.LightGray.copy(alpha = 0.5f),
                start = Offset(canvasX, 0f),
                end = Offset(canvasX, height),
                strokeWidth = if (x == 0) 2f else 1f
            )
            
            if (x != 0 && scale > 30) {
                drawContext.canvas.nativeCanvas.drawText(
                    x.toString(),
                    canvasX + 5,
                    centerY - 5,
                    Paint().apply {
                        color = android.graphics.Color.GRAY
                        textSize = 30f
                    }
                )
            }
        }

        // Horizontal lines & numbers
        for (y in floor(bottomEdge).toInt()..ceil(topEdge).toInt()) {
            val canvasY = centerY - y * scale
            drawLine(
                color = if (y == 0) Color.Gray else Color.LightGray.copy(alpha = 0.5f),
                start = Offset(0f, canvasY),
                end = Offset(width, canvasY),
                strokeWidth = if (y == 0) 2f else 1f
            )

            if (y != 0 && scale > 30) {
                drawContext.canvas.nativeCanvas.drawText(
                    y.toString(),
                    centerX + 5,
                    canvasY - 5,
                    Paint().apply {
                        color = android.graphics.Color.GRAY
                        textSize = 30f
                    }
                )
            }
        }

        // --- Draw Function Path ---
        // Optimization: Calculate points for the current visible range
        val path = Path()
        val stepSize = 1.0 / (scale / 5.0) // Adaptive step size based on zoom
        
        var isFirst = true
        val startX = floor(leftEdge).toDouble() - 1
        val endX = ceil(rightEdge).toDouble() + 1
        
        // Using optimized range evaluator
        val points = MathEvaluator.evaluateRange(equation, startX, endX, stepSize)
        
        points.forEach { (x, y) ->
            val canvasX = centerX + (x * scale).toFloat()
            val canvasY = centerY - (y * scale).toFloat()
            
            // Limit drawing to slightly outside bounds to avoid path issues
            if (canvasY > -1000 && canvasY < height + 1000) {
                if (isFirst) {
                    path.moveTo(canvasX, canvasY)
                    isFirst = false
                } else {
                    path.lineTo(canvasX, canvasY)
                }
            } else {
                isFirst = true // Break the path if it goes too far out
            }
        }
        drawPath(path, Color(0xFF2196F3), style = Stroke(width = 4f))

        // --- Draw Iteration Points ---
        steps.forEachIndexed { index, step ->
            val px = centerX + (step.xr * scale).toFloat()
            val py = centerY - (step.fxr * scale).toFloat()
            
            drawCircle(Color(0xFFE91E63), radius = 8f, center = Offset(px, py))

            if (selectedMethod == "Bisection" && step.a != null && step.b != null) {
                val ax = centerX + (step.a * scale).toFloat()
                val bx = centerX + (step.b * scale).toFloat()
                drawLine(Color(0x4CAF5066), Offset(ax, centerY), Offset(bx, centerY), 15f)
            }

            if (selectedMethod == "Newton-Raphson" && index < steps.size - 1) {
                val nextStep = steps[index + 1]
                val pNextX = centerX + (nextStep.xr * scale).toFloat()
                drawLine(
                    color = Color(0x9C27B088),
                    start = Offset(px, py),
                    end = Offset(pNextX, centerY),
                    strokeWidth = 3f
                )
            }
        }
    }
}
