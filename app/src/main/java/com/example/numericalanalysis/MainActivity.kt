package com.example.numericalanalysis

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.SpaceDashboard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.numericalanalysis.ui.navigation.NavGraph
import com.example.numericalanalysis.ui.navigation.Screen
import com.example.numericalanalysis.ui.theme.AppSettings
import com.example.numericalanalysis.ui.theme.NumericalAnalysisTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var appSettings by remember { mutableStateOf(AppSettings()) }
            
            NumericalAnalysisTheme(settings = appSettings) {
                MainApp(
                    settings = appSettings,
                    onSettingsChange = { appSettings = it }
                )
            }
        }
    }
}

@Composable
fun MainApp(
    settings: AppSettings,
    onSettingsChange: (AppSettings) -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val items = listOf(
        NavigationItem("Home", Screen.Dashboard.route, Icons.Default.SpaceDashboard),
        NavigationItem("Roots", Screen.RootFinding.route, Icons.Default.Functions),
        NavigationItem("Algebra", Screen.LinearAlgebra.route, Icons.Default.GridOn),
        NavigationItem("Compare", Screen.Comparison.route, Icons.Default.CompareArrows)
    )

    Scaffold(
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .navigationBarsPadding(),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                shape = RoundedCornerShape(32.dp),
                tonalElevation = 10.dp,
                shadowElevation = 15.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
            ) {
                NavigationBar(
                    containerColor = Color.Transparent,
                    tonalElevation = 0.dp,
                    windowInsets = WindowInsets(0, 0, 0, 0),
                    modifier = Modifier.height(72.dp)
                ) {
                    items.forEach { item ->
                        val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            icon = { 
                                Icon(
                                    item.icon, 
                                    contentDescription = item.title,
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.size(26.dp)
                                ) 
                            },
                            label = { 
                                Text(
                                    item.title,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Medium,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                ) 
                            },
                            selected = isSelected,
                            alwaysShowLabel = true,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavGraph(
            navController = navController,
            settings = settings,
            onSettingsChange = onSettingsChange,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        )
    }
}

data class NavigationItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)
