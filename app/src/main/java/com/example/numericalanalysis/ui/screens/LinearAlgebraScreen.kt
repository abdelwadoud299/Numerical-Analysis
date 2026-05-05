package com.example.numericalanalysis.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.numericalanalysis.ui.components.GlassCard
import com.example.numericalanalysis.ui.components.GlassTopBar
import com.example.numericalanalysis.ui.components.MatrixInput
import com.example.numericalanalysis.ui.components.MatrixStepView
import com.example.numericalanalysis.ui.theme.LocalAppSettings
import com.example.numericalanalysis.ui.viewmodel.LinearAlgebraViewModel
import com.example.numericalanalysis.ui.viewmodel.LinearAlgebraViewModelImpl

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun LinearAlgebraScreen(
    viewModel: LinearAlgebraViewModel = viewModel<LinearAlgebraViewModelImpl>(),
    onNavigateBack: () -> Unit = {}
) {
    val matrixSize by viewModel.matrixSize.collectAsState()
    val matrix by viewModel.matrix.collectAsState()
    val constants by viewModel.constants.collectAsState()
    val steps by viewModel.steps.collectAsState()
    val solutions by viewModel.solutions.collectAsState()
    val isProcessing by viewModel.isProcessing.collectAsState()
    val error by viewModel.error.collectAsState()

    var selectedMethod by remember { mutableStateOf("Gauss") }
    val methods = listOf("Gauss", "LU", "Cramer", "Gauss-Jordan")

    val settings = LocalAppSettings.current

    Scaffold(
        topBar = {
            GlassTopBar(
                title = "Numerical Lab",
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
            // Header Section
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Linear Algebra Solver",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Solve systems of linear equations using direct and decomposition methods.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Matrix Configuration & Input
            GlassCard {
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("System Configuration", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("Matrix size and values", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                                .padding(4.dp)
                        ) {
                            IconButton(
                                onClick = { if (matrixSize > 2) viewModel.onSizeChanged(matrixSize - 1) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Decrease Size", modifier = Modifier.size(18.dp))
                            }
                            Text(
                                "$matrixSize \u00d7 $matrixSize",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            IconButton(
                                onClick = { if (matrixSize < 6) viewModel.onSizeChanged(matrixSize + 1) },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Increase Size", modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    MatrixInput(
                        matrix = matrix,
                        constants = constants,
                        onMatrixValueChange = viewModel::updateMatrixElement,
                        onConstantValueChange = viewModel::updateConstantElement
                    )

                    // Method Selection Section
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Route, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("ALGORITHM SELECTION", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        
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
                                    Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                                        Text(method, style = MaterialTheme.typography.bodyMedium, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium)
                                    }
                                }
                            }
                        }
                    }

                    // Action Button
                    Button(
                        onClick = {
                            when (selectedMethod) {
                                "Gauss" -> viewModel.solveGauss()
                                "LU" -> viewModel.solveLU()
                                "Cramer" -> viewModel.solveCramer()
                                "Gauss-Jordan" -> viewModel.solveGaussJordan()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        if (isProcessing) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                        } else {
                            Icon(Icons.Default.Calculate, contentDescription = null)
                            Spacer(Modifier.width(12.dp))
                            Text("Solve System", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        }
                    }
                }
            }

            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(horizontal = 4.dp))
            }

            // Results Section
            solutions?.let { sol ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Final Solution Vector", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            sol.forEachIndexed { index, value ->
                                Column(
                                    modifier = Modifier.weight(1f).background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f), RoundedCornerShape(8.dp)).padding(8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("x${index + 1}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                                    Text("%.${settings.precision}f".format(value), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                                }
                            }
                        }
                    }
                }
            }

            if (steps.isNotEmpty()) {
                Text("Calculation Results", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                MatrixStepView(steps = steps)
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
