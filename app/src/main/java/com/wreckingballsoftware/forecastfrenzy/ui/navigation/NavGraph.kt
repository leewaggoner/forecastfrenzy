package com.wreckingballsoftware.forecastfrenzy.ui.navigation

import androidx.navigation.NavController

class NavGraph(navController: NavController) {
    val navigateToGameplayScreen: () -> Unit = {
        navController.navigate(
            Destinations.GameplayScreen
        )
    }
    val navigateToGameResultsScreen: () -> Unit = {
        navController.navigate(
            Destinations.GameResultsScreen
        )
    }
}