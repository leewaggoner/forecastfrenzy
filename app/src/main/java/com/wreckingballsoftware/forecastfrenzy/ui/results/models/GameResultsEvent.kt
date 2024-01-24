package com.wreckingballsoftware.forecastfrenzy.ui.results.models

sealed interface GameResultsEvent {
    data class Loading(val isLoading: Boolean) : GameResultsEvent
    data class InitResults(
        val buttonText: Int,
        val headlineText: Int,
        val currentRound: Int,
    ) : GameResultsEvent
    data object StartNextRound : GameResultsEvent
    data object GameOver : GameResultsEvent
    data class HandleGuess(val actualTemperature: Int) : GameResultsEvent
    data class ApiError(val message: String) : GameResultsEvent
    data object DismissErrorDialog : GameResultsEvent
}