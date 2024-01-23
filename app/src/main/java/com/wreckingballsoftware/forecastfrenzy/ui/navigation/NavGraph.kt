package com.wreckingballsoftware.forecastfrenzy.ui.navigation

import androidx.navigation.NavController

class NavGraph(navController: NavController) {
    val navigateToGameRulesScreen: () -> Unit = {
        navController.navigate(
            Destinations.GameRulesScreen
        )
    }
    val navigateToGameplayScreen: () -> Unit = {
        navController.navigate(
            Destinations.GameplayScreen
        ) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }
    val navigateToGameResultsScreen: (Int, Int, Int) -> Unit = { guess, bet, seconds ->
        navController.navigate(
            Destinations.GameResultsScreen.replace(
                oldValue = "{guess}",
                newValue = guess.toString()
            ).replace(
                oldValue = "{bet}",
                newValue = bet.toString()
            ).replace(
                oldValue = "{seconds}",
                newValue = seconds.toString()
            )
        ) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }
}