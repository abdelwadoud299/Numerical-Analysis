package com.example.numericalanalysis.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.numericalanalysis.data.model.IterationStep

import com.example.numericalanalysis.ui.theme.LocalAppSettings

@Composable
fun IterationTable(steps: List<IterationStep>) {
    val scrollState = rememberScrollState()
    val settings = LocalAppSettings.current
    val p = settings.precision

    GlassCard(modifier = Modifier.padding(vertical = 8.dp)) {
        Column(modifier = Modifier.horizontalScroll(scrollState)) {
            // Table Header
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                    .padding(vertical = 12.dp, horizontal = 8.dp)
            ) {
                TableCell(text = "ITER", width = 60.dp, isHeader = true)
                TableCell(text = "xr", width = 140.dp, isHeader = true)
                TableCell(text = "f(xr)", width = 140.dp, isHeader = true)
                TableCell(text = "ERROR", width = 140.dp, isHeader = true)
                TableCell(text = "a", width = 120.dp, isHeader = true)
                TableCell(text = "b", width = 120.dp, isHeader = true)
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            Column {
                steps.forEach { step ->
                    Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)) {
                        TableCell(text = step.iteration.toString(), width = 60.dp)
                        TableCell(text = "%.${p}f".format(step.xr), width = 140.dp)
                        TableCell(text = "%.${p}f".format(step.fxr), width = 140.dp)
                        TableCell(text = step.error?.let { "%.${p}f".format(it) } ?: "---", width = 140.dp)
                        TableCell(text = step.a?.let { "%.${p}f".format(it) } ?: "---", width = 120.dp)
                        TableCell(text = step.b?.let { "%.${p}f".format(it) } ?: "---", width = 120.dp)
                    }
                    HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                }
            }
        }
    }
}

@Composable
fun TableCell(
    text: String,
    width: androidx.compose.ui.unit.Dp,
    isHeader: Boolean = false
) {
    Text(
        text = text,
        modifier = Modifier.width(width),
        style = if (isHeader) MaterialTheme.typography.labelSmall else MaterialTheme.typography.bodySmall,
        fontFamily = if (isHeader) FontFamily.Default else FontFamily.Monospace,
        fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Medium,
        color = if (isHeader) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
        maxLines = 1
    )
}
