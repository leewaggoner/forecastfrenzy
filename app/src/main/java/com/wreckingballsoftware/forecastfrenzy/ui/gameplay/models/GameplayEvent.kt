package com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models

sealed interface GameplayEvent {
    data class InitRound(
        val curRound: Int,
        val playerPoints: Int,
        val roundPoints: Int,
        val antePoints: List<String>
    ) : GameplayEvent
    data class GuessChanged(val temperature: Float) : GameplayEvent
    data object DisplayResults : GameplayEvent
    data class StartRound(val city: String) : GameplayEvent
    data class ApiError(val message: String) : GameplayEvent
    data object DismissErrorDialog : GameplayEvent
    data class Loading(val isLoading: Boolean) : GameplayEvent
    data class AnteChanged(val newAnte: String) : GameplayEvent
}