package com.example.numericalanalysis.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.numericalanalysis.ui.theme.AppSettings
import com.example.numericalanalysis.ui.screens.*

sealed class Screen(val route: String, val title: String) {
    object Dashboard : Screen("dashboard", "Dashboard")
    object RootFinding : Screen("root_finding", "Roots")
    object LinearAlgebra : Screen("linear_algebra", "Algebra")
    object Comparison : Screen("comparison", "Compare")
    object Graphing : Screen("graphing", "Graph")
    object Settings : Screen("settings", "Settings")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    settings: AppSettings,
    onSettingsChange: (AppSettings) -> Unit,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Dashboard.route,
        modifier = modifier
    ) {
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToRootFinding = { navController.navigate(Screen.RootFinding.route) },
                onNavigateToLinearAlgebra = { navController.navigate(Screen.LinearAlgebra.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        composable(Screen.RootFinding.route) {
            RootFindingScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.LinearAlgebra.route) {
            LinearAlgebraScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.Comparison.route) {
            ComparisonScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.Graphing.route) {
            GraphingScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                settings = settings,
                onSettingsChange = onSettingsChange,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
