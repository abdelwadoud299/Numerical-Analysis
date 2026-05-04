package com.example.numericalanalysis.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.numericalanalysis.data.model.MatrixStep

@Composable
fun MatrixStepView(steps: List<MatrixStep>) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(steps) { step ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = step.stepDescription,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    MatrixDisplay(matrix = step.matrix, constants = step.b)
                }
            }
        }
    }
}

@Composable
fun MatrixDisplay(matrix: Array<DoubleArray>, constants: DoubleArray? = null) {
    Column {
        for (i in matrix.indices) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("[", style = MaterialTheme.typography.bodyMedium)
                for (j in matrix[i].indices) {
                    Text(
                        text = "%.2f".format(matrix[i][j]),
                        modifier = Modifier.width(50.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Text("]", style = MaterialTheme.typography.bodyMedium)
                
                constants?.let {
                    Text("|", style = MaterialTheme.typography.bodyMedium)
                    Text(
                        text = "%.2f".format(it[i]),
                        modifier = Modifier.width(50.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
