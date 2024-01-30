package com.wreckingballsoftware.forecastfrenzy.data.models

data class HighScoreError(
    val errorCode: Int = 0,
    val errorMessage: String = "",
)

data class HighScore(
    val name: String = "",
    val score: Int = 0,
)

data class HighScoresResponse(
    val scores: List<HighScore> = listOf(),
    val error: HighScoreError? = null,
)

data class UpdateHighScoreResponse(
    val success: Boolean = false,
    val error: HighScoreError? = null,
)

data class AddHighScoreResponse(
    val id: Long,
    val error: HighScoreError? = null,
)
