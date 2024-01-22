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
    var currentPlayerPoints = STARTING_PLAYER_POINTS
        private set
    var currentRoundPoints = CURRENT_ROUND_POINTS
        private set
    private var cityList = listOf<GameCity>()
    private var populationFilter = listOf(
        "population > 10000000",
        "population > 5000000 and population < 1000000",
        "population > 500000 and population < 1000000",
        "population > 250000 and population < 1000000",
        "population > 10000 and population < 250000",
    )

    fun startNewGame() {
        currentRound = 0
        cityList = listOf()
    }

    fun getCurrentAntePoints(): List<String> =
        (CURRENT_ANTE..CURRENT_ROUND_POINTS step 10).map { it.toString() }

    suspend fun startNewRound() {
        when (val result = cityRepo.getCities(populationFilter[currentRound])) {
            is ApiResult.Success -> {
                cityList = result.data ?: listOf()
            }
            else -> { }
        }
    }

    fun getCurrentCity(): String =
        if (cityList.isNotEmpty() && cityList.size > currentRound)
        {
            cityList[currentRound].name
        } else {
            ""
        }

    fun startTimer(onTick: () -> Unit, onFinish: () -> Unit) =
        gameTimer.startTimer(onTick, onFinish)

    fun handlePlayerAnswer() {

    }

    private fun getCityList() {

    }

    private fun getTempForCurrentCity() {

    }
}