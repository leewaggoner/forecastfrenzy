package com.wreckingballsoftware.forecastfrenzy.ui.results.models

sealed interface GameResultsNavigation {
    data object StartNextRound : GameResultsNavigation
}