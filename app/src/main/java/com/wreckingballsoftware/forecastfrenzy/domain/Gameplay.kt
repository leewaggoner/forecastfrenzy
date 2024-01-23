package com.wreckingballsoftware.forecastfrenzy.domain

import com.wreckingballsoftware.forecastfrenzy.data.ApiResult
import com.wreckingballsoftware.forecastfrenzy.data.CityRepo
import com.wreckingballsoftware.forecastfrenzy.data.WeatherRepo

const val MAX_TIME = 31
const val MAX_ROUNDS = 5
const val CURRENT_ANTE = 100
const val STARTING_PLAYER_POINTS = 1000
const val CURRENT_ROUND_POINTS = 200


class Gameplay(
    private val cityRepo: CityRepo,
    private val weatherRepo: WeatherRepo,
    private val gameTimer: GameTimer,
) {
    var currentRound = 0
        private set
    var currentRoundPoints = CURRENT_ROUND_POINTS
        private set
    private var city: GameCity? = null
    private val populationFilter = listOf(
        "population > 10000000",
        "population > 5000000 and population < 10000000",
        "population > 1000000 and population < 5000000",
        "population > 500000 and population < 1000000",
        "population > 1000 and population < 25000",
    )
    private val orderBy = listOf(
        "",
        "",
        "population ",
        "population ",
        "population ",
    )

    fun getCurrentCity(): String = city?.name ?: ""

    fun isGameOver() = currentRound == MAX_ROUNDS - 1

    fun advanceRound() {
        if (!isGameOver()) {
            currentRound++
        }
    }

    fun startNewGame() {
        currentRound = 0
    }

    fun getCurrentAntePoints(): List<String> =
        (CURRENT_ANTE..CURRENT_ROUND_POINTS step 10).map { it.toString() }

    suspend fun startNewRound() {
        when (val result = cityRepo.getCities(populationFilter[currentRound], orderBy[currentRound])) {
            is ApiResult.Success -> {
                city = result.data
            }
            else -> { }
        }
    }

    fun startTimer(onTick: () -> Unit, onFinish: () -> Unit) =
        gameTimer.startTimer(onTick, onFinish)

    fun stopTimer() = gameTimer.cancel()
}