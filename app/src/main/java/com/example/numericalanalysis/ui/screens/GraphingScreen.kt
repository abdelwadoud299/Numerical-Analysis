package com.example.numericalanalysis.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.numericalanalysis.ui.components.EquationInput
import com.example.numericalanalysis.ui.components.GraphView

@Composable
fun GraphingScreen() {
    var equation by remember { mutableStateOf("x^3 - 2*x - 5") }
    var graphKey by remember { mutableIntStateOf(0) } // Used to reset graph view

    val examples = listOf("x^2", "sin(x)", "exp(x)", "log(1+x)", "x^3 - x - 2")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            "Graphing Calculator",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Text(
            "Visualize your mathematical expressions in real-time.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                EquationInput(
                    value = equation,
                    onValueChange = { equation = it },
                    label = "Function f(x)"
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text("Try examples:", style = MaterialTheme.typography.labelSmall)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    examples.take(4).forEach { ex ->
                        SuggestionChip(
                            onClick = { equation = ex },
                            label = { Text(ex) }
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { graphKey++ }) {
                        Icon(Icons.Default.Refresh, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Reset View")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        // Graph Container
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            shape = MaterialTheme.shapes.large,
            tonalElevation = 2.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
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
                    shape = MaterialTheme.shapes.small,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(Color(0xFF2196F3), MaterialTheme.shapes.extraSmall)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("f(x)", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Analysis Details", 
            style = MaterialTheme.typography.titleMedium, 
            fontWeight = FontWeight.SemiBold
        )
        
        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                ListItem(
                    headlineContent = { Text("Interactive Graph") },
                    supportingContent = { Text("Pinch to zoom, drag with two fingers to pan. The grid adapts to your zoom level.") },
                    leadingContent = { Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary) }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                ListItem(
                    headlineContent = { Text("Standard Syntax") },
                    supportingContent = { Text("Supports polynomials (x^2), trig (sin, cos), and constants (pi, e).") },
                    leadingContent = { Icon(Icons.Default.Functions, contentDescription = null, tint = MaterialTheme.colorScheme.secondary) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
