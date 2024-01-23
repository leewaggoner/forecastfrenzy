package com.wreckingballsoftware.forecastfrenzy.ui.results.models

import com.wreckingballsoftware.forecastfrenzy.domain.BAD_TEMP_VALUE

data class GameResultsState(
    val currentRound: Int = 0,
    val buttonTextId: Int = 0,
    val headlineTextId: Int = 0,
    val isGameOver: Boolean = false,
    val cityName: String = "",
    val guess: Int = 0,
    val actualTemp: Int = BAD_TEMP_VALUE,
    val totalScore: Int = 0,
    val roundScore: Int = 0,
    val roundMaxPoints: Int = 0,
    val timeBonus: Int = 0,
    val bet: Int = 0,
    val isLoading: Boolean = false,
)