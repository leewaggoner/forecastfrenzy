package com.wreckingballsoftware.forecastfrenzy.ui.highscores.models

import com.wreckingballsoftware.forecastfrenzy.domain.models.HighScore

sealed interface HighScoresEvent {
    data class Loading(val isLoading: Boolean) : HighScoresEvent
    data class DisplayHighScores(val highScores: List<HighScore>) : HighScoresEvent
    data class ApiError(val errorMessage: String) : HighScoresEvent
    data object DismissErrorDialog : HighScoresEvent
}