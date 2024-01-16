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
    val navigateToGameResultsScreen: () -> Unit = {
        navController.navigate(
            Destinations.GameResultsScreen
        ) {
            popUpTo(navController.graph.id) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }
}