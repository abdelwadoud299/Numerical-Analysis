package com.example.numericalanalysis.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.numericalanalysis.ui.components.EquationInput
import com.example.numericalanalysis.ui.components.GraphView
import com.example.numericalanalysis.ui.components.IterationTable
import com.example.numericalanalysis.ui.viewmodel.RootFindingViewModelImpl
import com.example.numericalanalysis.util.export.ExportManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootFindingScreen(
    viewModel: RootFindingViewModelImpl = viewModel()
) {
    val context = LocalContext.current
    val equation by viewModel.equation.collectAsState()
    val gEquation by viewModel.gEquation.collectAsState()
    val steps by viewModel.steps.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val error by viewModel.error.collectAsState()
    val result by viewModel.result.collectAsState()

    var selectedMethod by remember { mutableStateOf("Bisection") }
    val methods = listOf("Bisection", "False Position", "Fixed Point", "Newton-Raphson", "Secant")
    
    var a by remember { mutableStateOf("-2") }
    var b by remember { mutableStateOf("2") }
    var x0 by remember { mutableStateOf("1") }
    var tol by remember { mutableStateOf("0.0001") }
    var maxIter by remember { mutableStateOf("100") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Numerical Lab", fontWeight = FontWeight.Bold) },
                actions = {
                    if (steps.isNotEmpty()) {
                        IconButton(onClick = { 
                            val uri = ExportManager.exportToPdf(context, "RootFinding", "Numerical Analysis Report", steps)
                            Toast.makeText(context, if (uri != null) "PDF Exported" else "Export Failed", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = "Export PDF")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.calculate(
                        method = selectedMethod,
                        a = a.toDoubleOrNull(),
                        b = b.toDoubleOrNull(),
                        x0 = x0.toDoubleOrNull(),
                        x1 = b.toDoubleOrNull(),
                        tolerance = tol.toDoubleOrNull() ?: 0.0001,
                        maxIterations = maxIter.toIntOrNull() ?: 100
                    )
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                expanded = !isProcessing,
                icon = { 
                    if (isProcessing) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                    else Icon(Icons.Default.Calculate, null) 
                },
                text = { Text("Calculate") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Method Selection
            Text("Select Method", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = selectedMethod,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    methods.forEach { method ->
                        DropdownMenuItem(
                            text = { Text(method) },
                            onClick = {
                                selectedMethod = method
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Input Section
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    EquationInput(value = equation, onValueChange = viewModel::onEquationChanged, label = "f(x)")
                    
                    if (selectedMethod == "Fixed Point") {
                        EquationInput(value = gEquation, onValueChange = viewModel::onGEquationChanged, label = "g(x)")
                    }

                    Row(Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (selectedMethod in listOf("Bisection", "False Position", "Secant")) {
                            OutlinedTextField(value = a, onValueChange = { a = it }, label = { Text("a / x0") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))
                            OutlinedTextField(value = b, onValueChange = { b = it }, label = { Text("b / x1") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))
                        } else {
                            OutlinedTextField(value = x0, onValueChange = { x0 = it }, label = { Text("Initial Guess") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))
                        }
                    }

                    Row(Modifier.fillMaxWidth().padding(top = 12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(value = tol, onValueChange = { tol = it }, label = { Text("Tolerance") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))
                        OutlinedTextField(value = maxIter, onValueChange = { maxIter = it }, label = { Text("Max Iter") }, modifier = Modifier.weight(1f), shape = RoundedCornerShape(12.dp))
                    }
                }
            }

            error?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp)) }

            // Results Section
            AnimatedVisibility(visible = result != null) {
                result?.let {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Root Found", style = MaterialTheme.typography.labelLarge)
                            Text("%.10f".format(it), style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }

            if (steps.isNotEmpty()) {
                Text("Visualization", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 16.dp))
                Box(Modifier.fillMaxWidth().height(300.dp).padding(vertical = 8.dp)) {
                    GraphView(equation = equation, steps = steps, selectedMethod = selectedMethod)
                }

                Text("Iteration Table", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                IterationTable(steps = steps)
            }
            
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
