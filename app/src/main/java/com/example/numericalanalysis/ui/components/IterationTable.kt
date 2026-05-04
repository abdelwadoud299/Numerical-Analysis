package com.example.numericalanalysis.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.numericalanalysis.data.model.IterationStep

@Composable
fun IterationTable(steps: List<IterationStep>) {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.fillMaxSize().horizontalScroll(scrollState)) {
        // Table Header
        Row(modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer).padding(8.dp)) {
            TableCell(text = "Iter", width = 60.dp)
            TableCell(text = "xr", width = 120.dp)
            TableCell(text = "f(xr)", width = 120.dp)
            TableCell(text = "Error", width = 120.dp)
            TableCell(text = "a", width = 100.dp)
            TableCell(text = "b", width = 100.dp)
        }
        
        HorizontalDivider()

        LazyColumn(modifier = Modifier.fillMaxHeight()) {
            items(steps) { step ->
                Row(modifier = Modifier.padding(8.dp)) {
                    TableCell(text = step.iteration.toString(), width = 60.dp)
                    TableCell(text = "%.6f".format(step.xr), width = 120.dp)
                    TableCell(text = "%.6f".format(step.fxr), width = 120.dp)
                    TableCell(text = step.error?.let { "%.6f".format(it) } ?: "---", width = 120.dp)
                    TableCell(text = step.a?.let { "%.4f".format(it) } ?: "---", width = 100.dp)
                    TableCell(text = step.b?.let { "%.4f".format(it) } ?: "---", width = 100.dp)
                }
                HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
            }
        }
    }
}

@Composable
fun TableCell(
    text: String,
    width: androidx.compose.ui.unit.Dp
) {
    Text(
        text = text,
        modifier = Modifier.width(width),
        fontSize = 14.sp,
        fontWeight = if (text.toIntOrNull() != null || text.contains(".")) FontWeight.Normal else FontWeight.Bold,
        maxLines = 1
    )
}
