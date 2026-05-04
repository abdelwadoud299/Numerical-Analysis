package com.example.numericalanalysis.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.numericalanalysis.data.model.MatrixStep

@Composable
fun MatrixStepView(steps: List<MatrixStep>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        steps.forEach { step ->
            GlassCard {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = step.stepDescription,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    MatrixDisplay(matrix = step.matrix, constants = step.b)
                }
            }
        }
    }
}

@Composable
fun MatrixDisplay(matrix: Array<DoubleArray>, constants: DoubleArray? = null) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (i in matrix.indices) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Matrix elements
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (j in matrix[i].indices) {
                        Text(
                            text = "%6.2f".format(matrix[i][j]),
                            modifier = Modifier.width(60.dp),
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                constants?.let {
                    // Vertical separator
                    Box(modifier = Modifier.width(1.dp).height(20.dp).background(MaterialTheme.colorScheme.outlineVariant))
                    
                    Text(
                        text = "%6.2f".format(it[i]),
                        modifier = Modifier.width(60.dp),
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
