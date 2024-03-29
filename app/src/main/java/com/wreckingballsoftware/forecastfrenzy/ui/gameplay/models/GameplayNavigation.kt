package com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models

sealed interface GameplayNavigation {
    data class ViewResults(
        val cityName: String,
        val guess: Int,
        val bet: Int,
        val seconds: Int,
    ): GameplayNavigation
}