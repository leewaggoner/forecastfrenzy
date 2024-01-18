package com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models

sealed interface GameplayEvent {
    data class GuessChanged(val temperature: Float) : GameplayEvent
    data object DisplayResults : GameplayEvent
    data class StartGame(val city: String, val temperature: Int) : GameplayEvent
}