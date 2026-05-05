package com.example.numericalanalysis.ui.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import com.example.numericalanalysis.data.model.IterationStep
import com.example.numericalanalysis.ui.theme.LocalAppSettings
import com.example.numericalanalysis.util.evaluator.MathEvaluator
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun GraphView(
    equation: String,
    modifier: Modifier = Modifier,
    steps: List<IterationStep> = emptyList(),
    selectedMethod: String = ""
) {
    var scale by remember { mutableFloatStateOf(50f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var tracerPosition by remember { mutableStateOf<Offset?>(null) }
    
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale = (scale * zoomChange).coerceIn(10f, 500f)
        offset += offsetChange
    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val tertiaryColor = MaterialTheme.colorScheme.tertiary
    
    val settings = LocalAppSettings.current
    val isDark = settings.isDarkMode

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .transformable(state = state)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, _ ->
                            tracerPosition = change.position
                        },
                        onDragEnd = { tracerPosition = null },
                        onDragCancel = { tracerPosition = null }
                    )
                }
        ) {
            val width = size.width
            val height = size.height
            val centerX = width / 2 + offset.x
            val centerY = height / 2 + offset.y

            // --- Draw Grid ---
            val leftEdge = -centerX / scale
            val rightEdge = (width - centerX) / scale
            val topEdge = centerY / scale
            val bottomEdge = (centerY - height) / scale

            // Grid Styling
            val gridColor = if (isDark) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f)
            val axisColor = if (isDark) Color.White.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.4f)

            // Vertical lines
            for (x in floor(leftEdge.toDouble()).toInt()..ceil(rightEdge.toDouble()).toInt()) {
                val canvasX = centerX + x * scale
                drawLine(
                    color = if (x == 0) axisColor else gridColor,
                    start = Offset(canvasX, 0f),
                    end = Offset(canvasX, height),
                    strokeWidth = if (x == 0) 2f else 1f
                )
            }

            // Horizontal lines
            for (y in floor(bottomEdge.toDouble()).toInt()..ceil(topEdge.toDouble()).toInt()) {
                val canvasY = centerY - y * scale
                drawLine(
                    color = if (y == 0) axisColor else gridColor,
                    start = Offset(0f, canvasY),
                    end = Offset(width, canvasY),
                    strokeWidth = if (y == 0) 2f else 1f
                )
            }

            // --- Draw Function Path ---
            val path = Path()
            val stepSize = 1.0 / (scale / 5.0)
            
            var isFirst = true
            val startX = floor(leftEdge.toDouble()) - 1
            val endX = ceil(rightEdge.toDouble()) + 1
            
            val points = MathEvaluator.evaluateRange(equation, startX, endX, stepSize)
            
            points.forEach { (x, y) ->
                val canvasX = centerX + (x * scale).toFloat()
                val canvasY = centerY - (y * scale).toFloat()
                
                if (canvasY > -1000 && canvasY < height + 1000) {
                    if (isFirst) {
                        path.moveTo(canvasX, canvasY)
                        isFirst = false
                    } else {
                        path.lineTo(canvasX, canvasY)
                    }
                } else {
                    isFirst = true
                }
            }
            
            // Function Glow Effect
            drawPath(
                path = path, 
                color = primaryColor.copy(alpha = 0.2f), 
                style = Stroke(width = 12f)
            )
            drawPath(
                path = path, 
                color = primaryColor, 
                style = Stroke(width = 4f)
            )

            // Detect Roots (where y changes sign)
            for (i in 0 until points.size - 1) {
                val p1 = points[i]
                val p2 = points[i+1]
                if ((p1.second >= 0 && p2.second <= 0) || (p1.second <= 0 && p2.second >= 0)) {
                    // Linear interpolation to find more precise root
                    val rootX = p1.first - p1.second * (p2.first - p1.first) / (p2.second - p1.second)
                    val canvasRootX = centerX + (rootX * scale).toFloat()
                    
                    drawCircle(
                        color = secondaryColor.copy(alpha = 0.4f),
                        radius = 12f,
                        center = Offset(canvasRootX, centerY)
                    )
                    drawCircle(
                        color = secondaryColor,
                        radius = 6f,
                        center = Offset(canvasRootX, centerY)
                    )
                }
            }

            // --- Draw Iteration Steps ---
            steps.forEachIndexed { index, step ->
                val px = centerX + (step.xr * scale).toFloat()
                val py = centerY - (step.fxr * scale).toFloat()
                drawCircle(tertiaryColor, radius = 8f, center = Offset(px, py))
            }

            // --- Draw Tracer ---
            tracerPosition?.let { pos ->
                val tracerX = (pos.x - centerX) / scale
                val tracerY = MathEvaluator.evaluate(equation, tracerX.toDouble())
                val tracerCanvasY = centerY - (tracerY * scale).toFloat()
                
                // Tracer Crosshair
                drawLine(secondaryColor.copy(alpha = 0.5f), Offset(pos.x, 0f), Offset(pos.x, height), 1f)
                drawLine(secondaryColor.copy(alpha = 0.5f), Offset(0f, tracerCanvasY), Offset(width, tracerCanvasY), 1f)
                
                // Dot on line
                drawCircle(secondaryColor, radius = 10f, center = Offset(pos.x, tracerCanvasY))
                
                // Label
                drawContext.canvas.nativeCanvas.drawText(
                    String.format("x: %.3f, y: %.3f", tracerX, tracerY),
                    pos.x + 20,
                    tracerCanvasY - 20,
                    Paint().apply {
                        color = if (isDark) android.graphics.Color.WHITE else android.graphics.Color.BLACK
                        textSize = 40f
                        typeface = Typeface.DEFAULT_BOLD
                    }
                )
            }
        }
    }
}
