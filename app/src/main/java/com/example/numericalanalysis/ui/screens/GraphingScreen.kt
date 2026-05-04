package com.example.numericalanalysis.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.numericalanalysis.ui.components.EquationInput
import com.example.numericalanalysis.ui.components.GlassCard
import com.example.numericalanalysis.ui.components.GlassTopBar
import com.example.numericalanalysis.ui.components.GraphView

@Composable
fun GraphingScreen(
    onNavigateBack: () -> Unit = {}
) {
    var equation by remember { mutableStateOf("x^3 - 2*x - 5") }
    var graphKey by remember { mutableIntStateOf(0) } // Used to reset graph view

    val examples = listOf("x^2", "sin(x)", "exp(x)", "log(1+x)", "x^3 - x - 2")

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
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Graphing Calculator",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    "Visualize your mathematical expressions in real-time.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            GlassCard {
                Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    EquationInput(
                        value = equation,
                        onValueChange = { equation = it },
                        label = "OBJECTIVE FUNCTION"
                    )
                    
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("TRY EXAMPLES", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outline)
                        @OptIn(ExperimentalLayoutApi::class)
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            examples.forEach { ex ->
                                SuggestionChip(
                                    onClick = { equation = ex },
                                    label = { Text(ex) },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = SuggestionChipDefaults.suggestionChipColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        labelColor = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = { graphKey++ },
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(6.dp))
                            Text("Reset View", style = MaterialTheme.typography.labelLarge)
                        }
                    }
                }
            }

            // Graph Container
            GlassCard {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    key(graphKey) {
                        GraphView(
                            equation = equation,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    
                    // Legend Overlay
                    Surface(
                        modifier = Modifier.align(Alignment.TopEnd).padding(12.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        shape = RoundedCornerShape(8.dp),
                        shadowElevation = 2.dp,
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                            Text("f(x)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    "Analysis Details", 
                    style = MaterialTheme.typography.titleMedium, 
                    fontWeight = FontWeight.Bold
                )
                
                GlassCard {
                    Column {
                        ListItem(
                            headlineContent = { Text("Interactive Graph", fontWeight = FontWeight.SemiBold) },
                            supportingContent = { Text("Pinch to zoom, drag with two fingers to pan. The grid adapts to your zoom level.") },
                            leadingContent = { Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        ListItem(
                            headlineContent = { Text("Standard Syntax", fontWeight = FontWeight.SemiBold) },
                            supportingContent = { Text("Supports polynomials (x^2), trig (sin, cos), and constants (pi, e).") },
                            leadingContent = { Icon(Icons.Default.Functions, contentDescription = null, tint = MaterialTheme.colorScheme.secondary) },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}
