package com.example.numericalanalysis.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.numericalanalysis.ui.screens.*

sealed class Screen(val route: String, val title: String) {
    object RootFinding : Screen("root_finding", "Roots")
    object LinearAlgebra : Screen("linear_algebra", "Algebra")
    object Comparison : Screen("comparison", "Compare")
    object Graphing : Screen("graphing", "Graph")
}

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.RootFinding.route,
        modifier = modifier
    ) {
        composable(Screen.RootFinding.route) {
            RootFindingScreen()
        }
        composable(Screen.LinearAlgebra.route) {
            LinearAlgebraScreen()
        }
        composable(Screen.Comparison.route) {
            ComparisonScreen()
        }
        composable(Screen.Graphing.route) {
            GraphingScreen()
        }
    }
}
