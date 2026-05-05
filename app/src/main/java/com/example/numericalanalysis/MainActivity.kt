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
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.numericalanalysis.ui.components.GlassCard
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.numericalanalysis.ui.navigation.NavGraph
import com.example.numericalanalysis.ui.navigation.Screen
import com.example.numericalanalysis.ui.theme.AppSettings
import com.example.numericalanalysis.ui.theme.NumericalAnalysisTheme
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.toArgb

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val appSettingsSaver = Saver<AppSettings, Map<String, Any>>(
                save = { mapOf("dark" to it.isDarkMode, "color" to it.accentColor.toArgb(), "prec" to it.precision) },
                restore = { AppSettings(it["dark"] as Boolean, Color(it["color"] as Int), it["prec"] as Int) }
            )
            var appSettings by rememberSaveable(stateSaver = appSettingsSaver) { mutableStateOf(AppSettings()) }
            
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
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
                tonalElevation = 8.dp,
                shadowElevation = 16.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .height(72.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items.forEach { item ->
                        val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    navController.navigate(item.route) {
                                        if (item.route == Screen.Dashboard.route) {
                                            popUpTo(navController.graph.id) { inclusive = true }
                                        } else {
                                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        }
                                        launchSingleTop = true
                                        restoreState = item.route != Screen.Dashboard.route
                                    }
                                }
                                .padding(vertical = 8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                modifier = Modifier.size(24.dp),
                                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) androidx.compose.ui.text.font.FontWeight.Bold else androidx.compose.ui.text.font.FontWeight.Medium,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    ) { _ ->
        NavGraph(
            navController = navController,
            settings = settings,
            onSettingsChange = onSettingsChange,
            modifier = Modifier // No more bottom padding here
        )
    }
}

data class NavigationItem(
    val title: String,
    val route: String,
    val icon: ImageVector
)
