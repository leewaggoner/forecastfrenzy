package com.wreckingballsoftware.forecastfrenzy.ui.navigation

import androidx.navigation.NavController

class NavGraph(navController: NavController) {
    val navigateToGameRulesScreen: () -> Unit = {
        navController.navigate(
            Destinations.GameRulesScreen
        ) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
            launchSingleTop = true
        }
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
    val navigateToGameResultsScreen: (String, Int, Int, Int) -> Unit = { cityName, guess, bet, seconds ->
        navController.navigate(
            Destinations.GameResultsScreen.replace(
                oldValue = "{cityName}",
                newValue = cityName
            ).replace(
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