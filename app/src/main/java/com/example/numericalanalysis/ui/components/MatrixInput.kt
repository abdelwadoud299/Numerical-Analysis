package com.example.numericalanalysis.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun MatrixInput(
    matrix: Array<DoubleArray>,
    constants: DoubleArray,
    onMatrixValueChange: (Int, Int, Double) -> Unit,
    onConstantValueChange: (Int, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val size = matrix.size

    Column(modifier = modifier.fillMaxWidth()) {
        Text("System of Equations (A|b)", style = MaterialTheme.typography.titleMedium)
        
        for (i in 0 until size) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Matrix Row
                for (j in 0 until size) {
                    var textValue by remember(matrix[i][j]) { mutableStateOf(matrix[i][j].toString()) }
                    OutlinedTextField(
                        value = textValue,
                        onValueChange = {
                            textValue = it
                            it.toDoubleOrNull()?.let { v -> onMatrixValueChange(i, j, v) }
                        },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    if (j < size - 1) Text("+")
                }
                
                Text("x${i+1} =")
                
                // Constant Vector Element
                var constValue by remember(constants[i]) { mutableStateOf(constants[i].toString()) }
                OutlinedTextField(
                    value = constValue,
                    onValueChange = {
                        constValue = it
                        it.toDoubleOrNull()?.let { v -> onConstantValueChange(i, v) }
                    },
                    modifier = Modifier.width(80.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        }
    }
}
