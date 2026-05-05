package com.example.numericalanalysis.ui.screens

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.numericalanalysis.ui.components.EquationInput
import com.example.numericalanalysis.ui.components.GlassCard
import com.example.numericalanalysis.ui.components.GlassTopBar
import com.example.numericalanalysis.ui.components.GraphView
import com.example.numericalanalysis.ui.components.IterationTable
import com.example.numericalanalysis.ui.theme.LocalAppSettings
import com.example.numericalanalysis.ui.viewmodel.RootFindingViewModelImpl
import com.example.numericalanalysis.util.export.ExportManager
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootFindingScreen(
    viewModel: RootFindingViewModelImpl = viewModel(),
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val equation by viewModel.equation.collectAsState()
    val gEquation by viewModel.gEquation.collectAsState()
    val steps by viewModel.steps.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val error by viewModel.error.collectAsState()
    val result by viewModel.result.collectAsState()

    var selectedMethod by remember { mutableStateOf("Newton-Raphson") }
    val methods = listOf("Newton-Raphson", "Bisection", "Secant", "False Position", "Fixed Point")
    
    var a by remember { mutableStateOf("-2") }
    var b by remember { mutableStateOf("2") }
    var x0 by remember { mutableStateOf("1") }
    var x1 by remember { mutableStateOf("2") }
    var toleranceText by remember { mutableStateOf("0.0001") }
    var maxIter by remember { mutableStateOf("100") }

    val tol = toleranceText.toDoubleOrNull() ?: 0.0001

    Scaffold(
        topBar = {
            GlassTopBar(
                title = "Numerical Lab",
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
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
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "Root-Finding Analysis",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )

            // Function Input Card
            GlassCard {
                EquationInput(
                    value = equation,
                    onValueChange = viewModel::onEquationChanged,
                    label = "OBJECTIVE FUNCTION"
                )
                
                if (selectedMethod == "Fixed Point") {
                    Spacer(modifier = Modifier.height(16.dp))
                    EquationInput(
                        value = gEquation,
                        onValueChange = viewModel::onGEquationChanged,
                        label = "TRANSFORMED FUNCTION",
                        prefix = "g(x) ="
                    )
                }
            }

            // Method Selection Card
            GlassCard {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Route, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("ALGORITHM SELECTION", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    
                    @OptIn(ExperimentalLayoutApi::class)
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        methods.forEach { method ->
                            val isSelected = selectedMethod == method
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable { selectedMethod = method },
                                color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                border = BorderStroke(
                                    1.dp, 
                                    if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                                ),
                                contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            ) {
                                Box(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        method, 
                                        style = MaterialTheme.typography.bodyMedium, 
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Parameters Card
            GlassCard {
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("COMPUTATIONAL PARAMETERS", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        ParameterInput(
                            label = "Tolerance (ε)", 
                            value = toleranceText, 
                            onValueChange = { toleranceText = it },
                            modifier = Modifier.weight(1f)
                        )
                        ParameterInput(
                            label = "Max Iterations", 
                            value = maxIter, 
                            onValueChange = { maxIter = it },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    // Numeric Inputs based on method
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        when (selectedMethod) {
                            "Bisection", "False Position" -> {
                                ParameterInput(label = "Lower Bound (a)", value = a, onValueChange = { a = it }, modifier = Modifier.weight(1f))
                                ParameterInput(label = "Upper Bound (b)", value = b, onValueChange = { b = it }, modifier = Modifier.weight(1f))
                            }
                            "Secant" -> {
                                ParameterInput(label = "First Guess (x0)", value = a, onValueChange = { a = it }, modifier = Modifier.weight(1f))
                                ParameterInput(label = "Second Guess (x1)", value = b, onValueChange = { b = it }, modifier = Modifier.weight(1f))
                            }
                            "Newton-Raphson", "Fixed Point" -> {
                                ParameterInput(label = "Initial Guess (x0)", value = x0, onValueChange = { x0 = it }, modifier = Modifier.weight(1f))
                            }
                        }
                    }

                    // Calculate Button
                    Button(
                        onClick = {
                            viewModel.calculate(
                                method = selectedMethod,
                                a = a.toDoubleOrNull(),
                                b = b.toDoubleOrNull(),
                                x0 = x0.toDoubleOrNull(),
                                x1 = x1.toDoubleOrNull(),
                                tolerance = tol,
                                maxIterations = maxIter.toIntOrNull() ?: 100
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                            Spacer(Modifier.width(12.dp))
                            Text("Processing...")
                        } else {
                            Icon(Icons.Default.Calculate, contentDescription = null)
                            Spacer(Modifier.width(12.dp))
                            Text("Run Analysis", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }

            error?.let { 
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(horizontal = 4.dp)) 
            }

            // Results Section
            val settings = LocalAppSettings.current
            AnimatedVisibility(visible = result != null) {
                result?.let {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Root Found", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                            Text(
                                "%.${settings.precision}f".format(it),
                                style = MaterialTheme.typography.headlineMedium.copy(fontFamily = FontFamily.Monospace),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            if (steps.isNotEmpty()) {
                Text("Visualization", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Box(Modifier.fillMaxWidth().height(300.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.surface)) {
                    GraphView(equation = equation, steps = steps, selectedMethod = selectedMethod)
                }

                Text("Iteration Table", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                IterationTable(steps = steps, method = selectedMethod)
            }
            
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun ParameterInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.03f),
                focusedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}
