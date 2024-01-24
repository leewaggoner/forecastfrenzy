package com.wreckingballsoftware.forecastfrenzy.ui.navigation

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wreckingballsoftware.forecastfrenzy.R
import com.wreckingballsoftware.forecastfrenzy.domain.BAD_TEMP_VALUE
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.GameplayScreen
import com.wreckingballsoftware.forecastfrenzy.ui.results.GameResultsScreen
import com.wreckingballsoftware.forecastfrenzy.ui.rules.GameRulesScreen

@Composable
fun ForecastHost(connectionState: Boolean) {
    val navController = rememberNavController()
    val navGraph = remember(navController) { NavGraph(navController) }

    val startDestination = Destinations.GameRulesScreen

    if (!connectionState) {
        Toast.makeText(LocalContext.current, R.string.network_warning, Toast.LENGTH_LONG)
            .show()
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = Destinations.GameRulesScreen) {
            GameRulesScreen(navGraph = navGraph)
        }

        composable(route = Destinations.GameplayScreen) {
            GameplayScreen(navGraph = navGraph)
        }

        composable(
            route = Destinations.GameResultsScreen,
            arguments = listOf(
                navArgument("cityName") { type = NavType.StringType },
                navArgument("guess") { type = NavType.IntType },
                navArgument("bet") { type = NavType.IntType },
                navArgument("seconds") { type = NavType.IntType },
            )
        ) {backstackEntry ->
            val cityName: String = backstackEntry.arguments?.getString("cityName") ?: ""
            val guess: Int = backstackEntry.arguments?.getInt("guess") ?: BAD_TEMP_VALUE
            val bet: Int = backstackEntry.arguments?.getInt("bet") ?: 0
            val seconds: Int = backstackEntry.arguments?.getInt("seconds") ?: 0
            GameResultsScreen(
                navGraph = navGraph,
                cityName = cityName,
                guess = guess,
                bet = bet,
                seconds = seconds,
            )
        }
    }
}