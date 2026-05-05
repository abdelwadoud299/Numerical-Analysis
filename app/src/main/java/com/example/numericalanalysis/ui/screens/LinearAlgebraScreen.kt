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
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    when (selectedMethod) {
                        "Gauss" -> viewModel.solveGauss()
                        "LU" -> viewModel.solveLU()
                        "Cramer" -> viewModel.solveCramer()
                        "Gauss-Jordan" -> viewModel.solveGaussJordan()
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.size(56.dp)
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.Functions, contentDescription = "Solve", modifier = Modifier.size(28.dp))
                }
            }
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
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Matrix Input", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("Configure dimensions", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
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
                                "$matrixSize × $matrixSize",
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

                    Spacer(modifier = Modifier.height(20.dp))

                    MatrixInput(
                        matrix = matrix,
                        constants = constants,
                        onMatrixValueChange = viewModel::updateMatrixElement,
                        onConstantValueChange = viewModel::updateConstantElement
                    )
                }
            }

            // Method Selection
            GlassCard {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
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
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { selectedMethod = method },
                                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.primary.copy(alpha = 0.03f),
                                border = if (isSelected) null else BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                                contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (isSelected) {
                                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                                    }
                                    Text(method, style = MaterialTheme.typography.bodyMedium, fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal)
                                }
                            }
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
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Final Solution", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onTertiaryContainer)
                        sol.forEachIndexed { index, value ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("x${index + 1}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                                Text("%.${settings.precision}f".format(value), style = MaterialTheme.typography.bodyMedium, fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace)
                            }
                        }
                    }
                }
            }

            if (steps.isNotEmpty()) {
                Text("Calculation Steps", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                MatrixStepView(steps = steps)
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
