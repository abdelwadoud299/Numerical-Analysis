package com.example.numericalanalysis.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.numericalanalysis.data.model.IterationStep
import androidx.compose.foundation.isSystemInDarkTheme
import com.example.numericalanalysis.ui.components.EquationInput
import com.example.numericalanalysis.ui.components.GlassCard
import com.example.numericalanalysis.ui.components.GlassTopBar
import com.example.numericalanalysis.ui.components.GraphView
import com.example.numericalanalysis.ui.theme.*
import com.example.numericalanalysis.ui.viewmodel.RootFindingViewModel
import com.example.numericalanalysis.ui.viewmodel.RootFindingViewModelImpl

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ComparisonScreen(
    onNavigateBack: () -> Unit = {}
) {
    val viewModel1: RootFindingViewModel = remember { RootFindingViewModelImpl() }
    val viewModel2: RootFindingViewModel = remember { RootFindingViewModelImpl() }

    var equation by remember { mutableStateOf("x^3 - x - 1") }
    var a by remember { mutableStateOf("1.0") }
    var b by remember { mutableStateOf("2.0") }
    
    var method1 by remember { mutableStateOf("Bisection") }
    var method2 by remember { mutableStateOf("Newton-Raphson") }
    val methods = listOf("Bisection", "False Position", "Fixed Point", "Newton-Raphson", "Secant")

    val steps1 by viewModel1.steps.collectAsState()
    val steps2 by viewModel2.steps.collectAsState()
    val isProcessing1 by viewModel1.isProcessing.collectAsState()
    val isProcessing2 by viewModel2.isProcessing.collectAsState()
    val time1 by viewModel1.executionTimeMs.collectAsState()
    val time2 by viewModel2.executionTimeMs.collectAsState()

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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Summary Card
            GlassCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Comparison Mode", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline, fontWeight = FontWeight.SemiBold)
                        Text("Root Finding Analysis", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }

                    if (steps1.isNotEmpty() && steps2.isNotEmpty()) {
                        val winner = if (steps1.size <= steps2.size) method1 else method2
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(100),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.EmojiEvents, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                                Text("$winner: Fastest", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Input Section
            GlassCard {
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    EquationInput(
                        value = equation,
                        onValueChange = { equation = it },
                        label = "COMPARE FUNCTIONS"
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        MethodDropdown("Method A", method1, methods, Modifier.weight(1f)) { method1 = it }
                        MethodDropdown("Method B", method2, methods, Modifier.weight(1f)) { method2 = it }
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ParameterInput(label = "Start (a/x0)", value = a, onValueChange = { a = it }, modifier = Modifier.weight(1f))
                        ParameterInput(label = "End (b/x1)", value = b, onValueChange = { b = it }, modifier = Modifier.weight(1f))
                    }

                    Button(
                        onClick = {
                            viewModel1.onEquationChanged(equation)
                            viewModel2.onEquationChanged(equation)
                            viewModel1.calculate(method1, a.toDoubleOrNull(), b.toDoubleOrNull(), a.toDoubleOrNull(), b.toDoubleOrNull(), 0.0001, 100)
                            viewModel2.calculate(method2, a.toDoubleOrNull(), b.toDoubleOrNull(), a.toDoubleOrNull(), b.toDoubleOrNull(), 0.0001, 100)
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isProcessing1 && !isProcessing2
                    ) {
                        if (isProcessing1 || isProcessing2) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CompareArrows, contentDescription = null)
                                Text("Analyze & Compare", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Convergence Graph
            AnimatedVisibility(visible = steps1.isNotEmpty() || steps2.isNotEmpty()) {
                GlassCard {
                    Text("Convergence Trajectory", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(240.dp).clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.surface)) {
                        GraphView(equation = equation, steps = steps1 + steps2)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                        LegendItem(method1, MaterialTheme.colorScheme.secondary)
                        Spacer(modifier = Modifier.width(24.dp))
                        LegendItem(method2, MaterialTheme.colorScheme.primary)
                    }
                }
            }

            // Metrics Grid
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ComparisonMetricCard(
                    label = "Method A: $method1",
                    steps = steps1,
                    time = time1,
                    accentColor = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.weight(1f)
                )
                ComparisonMetricCard(
                    label = "Method B: $method2",
                    steps = steps2,
                    time = time2,
                    accentColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(100)).background(color))
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun ComparisonMetricCard(
    label: String,
    steps: List<IterationStep>,
    time: Long,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val settings = LocalAppSettings.current
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isDark) GlassBackgroundDark else GlassBackgroundLight)
            .border(1.dp, if (isDark) GlassBorderDark else GlassBorderLight, RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.width(3.dp).height(24.dp).clip(RoundedCornerShape(100)).background(accentColor))
                Spacer(modifier = Modifier.width(10.dp))
                Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            }

            if (steps.isNotEmpty()) {
                val precision = settings.precision
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    MetricItem("Iterations", steps.size.toString(), accentColor)
                    MetricItem("Time (ms)", time.toString(), accentColor)
                    MetricItem("Final Root", "%.${precision}f".format(steps.last().xr), accentColor)
                    MetricItem("Final Error", steps.last().error?.let { "%.2e".format(it) } ?: "N/A", accentColor)
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No results yet", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun MetricItem(label: String, value: String, accentColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, color = accentColor)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MethodDropdown(label: String, selected: String, options: List<String>, modifier: Modifier, onSelected: (String) -> Unit) {
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
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary
            )
        )
        ExposedDropdownMenu(
            expanded = expanded, 
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, style = MaterialTheme.typography.bodyMedium) }, 
                    onClick = { onSelected(option); expanded = false }
                )
            }
        }
    }
}
