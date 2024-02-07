package com.wreckingballsoftware.forecastfrenzy.ui.results.models

sealed interface GameResultsEvent {
    data class Loading(val isLoading: Boolean) : GameResultsEvent
    data object InitResultsScreen : GameResultsEvent
    data object StartNextRound : GameResultsEvent
    data class UpdateHighScore(val highScore: Int) : GameResultsEvent
    data class HandleGuessResult(val actualTemperature: Int) : GameResultsEvent
    data object ViewHighScores : GameResultsEvent
    data class ApiError(val message: String) : GameResultsEvent
    data object DismissErrorDialog : GameResultsEvent
}