package com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models

sealed interface GameplayNavigation {
    data object ViewResults: GameplayNavigation
}