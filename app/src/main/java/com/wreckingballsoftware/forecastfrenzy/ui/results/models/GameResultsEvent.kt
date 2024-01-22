package com.wreckingballsoftware.forecastfrenzy.ui.results.models

sealed interface GameResultsEvent {
    data object StartNextRound : GameResultsEvent
    data object GameOver : GameResultsEvent
}