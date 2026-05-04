package com.example.numericalanalysis.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.numericalanalysis.ui.components.IterationTable
import com.example.numericalanalysis.ui.viewmodel.RootFindingViewModel
import com.example.numericalanalysis.ui.viewmodel.RootFindingViewModelImpl

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComparisonScreen() {
    val viewModel1: RootFindingViewModel = remember { RootFindingViewModelImpl() }
    val viewModel2: RootFindingViewModel = remember { RootFindingViewModelImpl() }

    var equation by remember { mutableStateOf("x^3 - x - 1") }
    var a by remember { mutableStateOf("1.0") }
    var b by remember { mutableStateOf("2.0") }
    var x0 by remember { mutableStateOf("1.5") }
    var tol by remember { mutableStateOf("0.0001") }

    var method1 by remember { mutableStateOf("Bisection") }
    var method2 by remember { mutableStateOf("Newton-Raphson") }
    val methods = listOf("Bisection", "False Position", "Fixed Point", "Newton-Raphson", "Secant")

    val steps1 by viewModel1.steps.collectAsState()
    val steps2 by viewModel2.steps.collectAsState()
    val time1 by viewModel1.executionTimeMs.collectAsState()
    val time2 by viewModel2.executionTimeMs.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Text("Method Comparison", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = equation,
            onValueChange = { equation = it },
            label = { Text("f(x)") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MethodSelectorComp("Method 1", method1, methods, Modifier.weight(1f)) { method1 = it }
            MethodSelectorComp("Method 2", method2, methods, Modifier.weight(1f)) { method2 = it }
        }

        Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(value = a, onValueChange = { a = it }, label = { Text("a / x0") }, modifier = Modifier.weight(1f))
            OutlinedTextField(value = b, onValueChange = { b = it }, label = { Text("b / x1") }, modifier = Modifier.weight(1f))
        }

        Button(
            onClick = {
                viewModel1.onEquationChanged(equation)
                viewModel2.onEquationChanged(equation)
                
                viewModel1.calculate(method1, a.toDoubleOrNull(), b.toDoubleOrNull(), x0.toDoubleOrNull(), b.toDoubleOrNull(), tol.toDoubleOrNull() ?: 0.001, 100)
                viewModel2.calculate(method2, a.toDoubleOrNull(), b.toDoubleOrNull(), x0.toDoubleOrNull(), b.toDoubleOrNull(), tol.toDoubleOrNull() ?: 0.001, 100)
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            if (viewModel1.isProcessing.collectAsState().value || viewModel2.isProcessing.collectAsState().value) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
            } else {
                Text("Run Comparison")
            }
        }

        Row(Modifier.fillMaxWidth().height(500.dp)) {
            Column(Modifier.weight(1f).padding(4.dp)) {
                Text(method1, style = MaterialTheme.typography.titleSmall)
                Text("Time: ${time1}ms", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                Box(Modifier.fillMaxSize()) { IterationTable(steps1) }
            }
            VerticalDivider(modifier = Modifier.padding(horizontal = 4.dp))
            Column(Modifier.weight(1f).padding(4.dp)) {
                Text(method2, style = MaterialTheme.typography.titleSmall)
                Text("Time: ${time2}ms", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                Box(Modifier.fillMaxSize()) { IterationTable(steps2) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MethodSelectorComp(label: String, selected: String, options: List<String>, modifier: Modifier, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = { onSelected(option); expanded = false })
            }
        }
    }
}
