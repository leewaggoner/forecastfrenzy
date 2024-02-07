package com.wreckingballsoftware.forecastfrenzy.ui.highscores.models

import com.wreckingballsoftware.forecastfrenzy.domain.models.HighScore

data class HighScoresState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val highScores: List<HighScore> = emptyList()
)
