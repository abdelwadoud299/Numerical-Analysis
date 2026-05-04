package com.example.numericalanalysis.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MatrixInput(
    matrix: Array<DoubleArray>,
    constants: DoubleArray,
    onMatrixValueChange: (Int, Int, Double) -> Unit,
    onConstantValueChange: (Int, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val matrixSize = matrix.size

    GlassCard(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                // Left Bracket Decoration
                Box(
                    modifier = Modifier
                        .width(12.dp)
                        .height(IntrinsicSize.Max)
                        .padding(vertical = 4.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidth = 2.dp.toPx()
                        val w = size.width
                        val h = size.height
                        
                        // Vertical line
                        drawLine(
                            color = Color(0xFF737783).copy(alpha = 0.5f),
                            start = Offset(strokeWidth / 2, 0f),
                            end = Offset(strokeWidth / 2, h),
                            strokeWidth = strokeWidth
                        )
                        // Top horizontal
                        drawLine(
                            color = Color(0xFF737783).copy(alpha = 0.5f),
                            start = Offset(0f, strokeWidth / 2),
                            end = Offset(w, strokeWidth / 2),
                            strokeWidth = strokeWidth
                        )
                        // Bottom horizontal
                        drawLine(
                            color = Color(0xFF737783).copy(alpha = 0.5f),
                            start = Offset(0f, h - strokeWidth / 2),
                            end = Offset(w, h - strokeWidth / 2),
                            strokeWidth = strokeWidth
                        )
                    }
                }

                // Grid for Matrix and Constants
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (i in 0 until matrixSize) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Matrix Elements
                            for (j in 0 until matrixSize) {
                                MatrixCell(
                                    value = matrix[i][j],
                                    onValueChange = { onMatrixValueChange(i, j, it) }
                                )
                            }
                            
                            // Divider bar like [A|b]
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(48.dp)
                                    .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                            )
                            
                            // Constant element
                            MatrixCell(
                                value = constants[i],
                                onValueChange = { onConstantValueChange(i, it) },
                                isConstant = true
                            )
                        }
                    }
                }

                // Right Bracket Decoration
                Box(
                    modifier = Modifier
                        .width(12.dp)
                        .height(IntrinsicSize.Max)
                        .padding(vertical = 4.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidth = 2.dp.toPx()
                        val w = size.width
                        val h = size.height
                        
                        // Vertical line
                        drawLine(
                            color = Color(0xFF737783).copy(alpha = 0.5f),
                            start = Offset(w - strokeWidth / 2, 0f),
                            end = Offset(w - strokeWidth / 2, h),
                            strokeWidth = strokeWidth
                        )
                        // Top horizontal
                        drawLine(
                            color = Color(0xFF737783).copy(alpha = 0.5f),
                            start = Offset(0f, strokeWidth / 2),
                            end = Offset(w, strokeWidth / 2),
                            strokeWidth = strokeWidth
                        )
                        // Bottom horizontal
                        drawLine(
                            color = Color(0xFF737783).copy(alpha = 0.5f),
                            start = Offset(0f, h - strokeWidth / 2),
                            end = Offset(w, h - strokeWidth / 2),
                            strokeWidth = strokeWidth
                        )
                    }
                }
            }

            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(100),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        Icons.Default.Info, 
                        contentDescription = null, 
                        modifier = Modifier.size(14.dp), 
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        "Tap a cell to edit. Values are auto-saved.", 
                        style = MaterialTheme.typography.labelSmall, 
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun MatrixCell(
    value: Double,
    onValueChange: (Double) -> Unit,
    isConstant: Boolean = false
) {
    var textValue by remember(value) { mutableStateOf(if (value == 0.0) "" else value.toString()) }
    
    Box(
        modifier = Modifier
            .size(width = 64.dp, height = 48.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(if (isConstant) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface)
            .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        BasicTextField(
            value = textValue,
            onValueChange = {
                textValue = it
                it.toDoubleOrNull()?.let { v -> onValueChange(v) }
            },
            textStyle = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            decorationBox = { innerTextField ->
                if (textValue.isEmpty()) {
                    Text(
                        "0",
                        style = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                innerTextField()
            }
        )
    }
}
