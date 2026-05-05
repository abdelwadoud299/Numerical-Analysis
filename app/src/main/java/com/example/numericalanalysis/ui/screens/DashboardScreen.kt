package com.example.numericalanalysis.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.numericalanalysis.ui.components.GlassCard
import com.example.numericalanalysis.ui.components.GlassTopBar

@Composable
fun DashboardScreen(
    onNavigateToRootFinding: () -> Unit,
    onNavigateToLinearAlgebra: () -> Unit,
    onNavigateToSettings: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            GlassTopBar(
                title = "Numerical Lab",
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
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
            // Welcome Section
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    "Home",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    "Select a numerical method to begin computation. This environment supports high-precision iterative solvers and linear system techniques.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Root-Finding Techniques Section
            DashboardSection(
                title = "Root-Finding Techniques",
                icon = Icons.Default.Functions,
                iconContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                iconColor = MaterialTheme.colorScheme.primary
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val rfColor = MaterialTheme.colorScheme.primary
                    MethodCard(
                        title = "Newton-Raphson",
                        description = "Fast converging method using derivatives. Efficient for smooth functions.",
                        rule = "Rule: Requires f'(x) ≠ 0 and continuous derivative.",
                        formula = "xₙ₊₁ = xₙ - f(xₙ) / f'(xₙ)",
                        accentColor = rfColor,
                        onClick = onNavigateToRootFinding
                    )
                    MethodCard(
                        title = "Bisection Method",
                        description = "Robust, bracketed root-finding. Guaranteed convergence.",
                        rule = "Rule: f(a)·f(b) < 0 must hold.",
                        formula = "c = (a + b) / 2",
                        accentColor = rfColor,
                        onClick = onNavigateToRootFinding
                    )
                    MethodCard(
                        title = "Secant Method",
                        description = "Approximates derivative using two points. No derivatives needed.",
                        rule = "Rule: Needs two distinct initial guesses x₀, x₁.",
                        formula = "xₙ₊₁ = xₙ - f(xₙ)Δx / Δf",
                        accentColor = rfColor,
                        onClick = onNavigateToRootFinding
                    )
                    MethodCard(
                        title = "False Position",
                        description = "Linear interpolation in a bracket. Faster than Bisection.",
                        rule = "Rule: Maintains a bracket where root exists.",
                        formula = "xᵣ = b - f(b)Δx / Δf",
                        accentColor = rfColor,
                        onClick = onNavigateToRootFinding
                    )
                    MethodCard(
                        title = "Fixed Point",
                        description = "Iterates x = g(x) to find root. Simple and direct.",
                        rule = "Rule: Converges if |g'(x)| < 1 near root.",
                        formula = "xₙ₊₁ = g(xₙ)",
                        accentColor = rfColor,
                        onClick = onNavigateToRootFinding
                    )
                }
            }

            // Linear Algebra Solvers Section
            DashboardSection(
                title = "Linear Algebra Solvers",
                icon = Icons.Default.GridOn,
                iconContainerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.1f),
                iconColor = MaterialTheme.colorScheme.secondary
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val laColor = MaterialTheme.colorScheme.secondary
                    MethodCard(
                        title = "Gauss Elimination",
                        description = "Direct solver via row reduction to upper triangular form.",
                        rule = "Rule: Partial pivoting for stability.",
                        formula = "[ A | b ] → [ I | x ]",
                        accentColor = laColor,
                        onClick = onNavigateToLinearAlgebra
                    )
                    MethodCard(
                        title = "LU Decomposition",
                        description = "Factors matrix into Lower and Upper triangular parts.",
                        rule = "Rule: Efficient for solving multiple right-hand sides.",
                        formula = "A = L · U",
                        accentColor = laColor,
                        onClick = onNavigateToLinearAlgebra
                    )
                    MethodCard(
                        title = "Cramer's Rule",
                        description = "Solver using determinants for small systems.",
                        rule = "Rule: det(A) ≠ 0 (system must have unique solution).",
                        formula = "xᵢ = det(Aᵢ) / det(A)",
                        accentColor = laColor,
                        onClick = onNavigateToLinearAlgebra
                    )
                    MethodCard(
                        title = "Gauss-Jordan",
                        description = "Extended row reduction to identity matrix. Finds inverse directly.",
                        rule = "Rule: Simultaneously finds A⁻¹ and solution x.",
                        formula = "[ A | I ] → [ I | A⁻¹ ]",
                        accentColor = laColor,
                        onClick = onNavigateToLinearAlgebra
                    )
                }
            }
            // Manual gap to ensure the last card clears the navbar
            Spacer(modifier = Modifier.navigationBarsPadding().height(88.dp))
        }
    }
}

@Composable
fun DashboardSection(
    title: String,
    icon: ImageVector,
    iconContainerColor: Color,
    iconColor: Color,
    content: @Composable () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconContainerColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
            }
            Text(
                title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        content()
    }
}

@Composable
fun MethodCard(
    title: String,
    description: String,
    rule: String,
    formula: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    GlassCard(
        modifier = modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = accentColor,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = accentColor.copy(alpha = 0.6f)
                )
            }
            
            Surface(
                color = accentColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    rule,
                    style = MaterialTheme.typography.labelMedium,
                    color = accentColor,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }

            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp,
                maxLines = 3,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(accentColor.copy(alpha = 0.05f))
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    formula,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = accentColor,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}
