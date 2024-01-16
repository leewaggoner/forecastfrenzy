package com.wreckingballsoftware.forecastfrenzy.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.GameplayScreen
import com.wreckingballsoftware.forecastfrenzy.ui.results.GameResultsScreen
import com.wreckingballsoftware.forecastfrenzy.ui.rules.GameRulesScreen

@Composable
fun ForecastHost() {
    val navController = rememberNavController()
    val navGraph = remember(navController) { NavGraph(navController) }

    val startDestination = Destinations.GameRulesScreen

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Destinations.GameRulesScreen) {
            GameRulesScreen(navGraph = navGraph)
        }

        composable(Destinations.GameplayScreen) {
            GameplayScreen(navGraph = navGraph)
        }

        composable(Destinations.GameResultsScreen) {
            GameResultsScreen(navGraph = navGraph)
        }
    }
}