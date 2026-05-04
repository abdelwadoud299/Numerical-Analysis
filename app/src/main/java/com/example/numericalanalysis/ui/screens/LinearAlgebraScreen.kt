package com.example.numericalanalysis.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.numericalanalysis.ui.components.MatrixInput
import com.example.numericalanalysis.ui.components.MatrixStepView
import com.example.numericalanalysis.ui.viewmodel.LinearAlgebraViewModel
import com.example.numericalanalysis.ui.viewmodel.LinearAlgebraViewModelImpl

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LinearAlgebraScreen(viewModel: LinearAlgebraViewModel = LinearAlgebraViewModelImpl()) {
    val matrixSize by viewModel.matrixSize.collectAsState()
    val matrix by viewModel.matrix.collectAsState()
    val constants by viewModel.constants.collectAsState()
    val steps by viewModel.steps.collectAsState()
    val solutions by viewModel.solutions.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Linear Algebra Solver", style = MaterialTheme.typography.headlineMedium)

        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Matrix Size: $matrixSize x $matrixSize", modifier = Modifier.padding(top = 12.dp))
            Row {
                IconButton(onClick = { if (matrixSize > 2) viewModel.onSizeChanged(matrixSize - 1) }) {
                    Text("-", style = MaterialTheme.typography.headlineSmall)
                }
                IconButton(onClick = { if (matrixSize < 6) viewModel.onSizeChanged(matrixSize + 1) }) {
                    Text("+", style = MaterialTheme.typography.headlineSmall)
                }
            }
        }

        MatrixInput(
            matrix = matrix,
            constants = constants,
            onMatrixValueChange = viewModel::updateMatrixElement,
            onConstantValueChange = viewModel::updateConstantElement
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = viewModel::solveGauss, enabled = !isProcessing) { Text("Gauss") }
            Button(onClick = viewModel::solveLU, enabled = !isProcessing) { Text("LU") }
            Button(onClick = viewModel::solveCramer, enabled = !isProcessing) { Text("Cramer") }
            Button(onClick = viewModel::solveGaussJordan, enabled = !isProcessing) { Text("Gauss-Jordan") }
        }

        if (isProcessing) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(8.dp))
        }

        solutions?.let { sol ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Final Solution:", style = MaterialTheme.typography.titleMedium)
                    sol.forEachIndexed { index, value ->
                        Text("x${index + 1} = %.4f".format(value))
                    }
                }
            }
        }

        if (steps.isNotEmpty()) {
            Text("Calculation Steps", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(vertical = 8.dp))
            Box(modifier = Modifier.heightIn(max = 600.dp)) {
                MatrixStepView(steps = steps)
            }
        }
    }
}
