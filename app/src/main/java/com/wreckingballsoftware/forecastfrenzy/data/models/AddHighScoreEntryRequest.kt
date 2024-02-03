package com.wreckingballsoftware.forecastfrenzy.data.models

data class AddHighScoreEntryRequest(
    val name: String,
    val score: Int = 0,
)
