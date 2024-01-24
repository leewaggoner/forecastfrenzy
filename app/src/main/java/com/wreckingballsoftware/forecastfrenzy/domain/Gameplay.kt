package com.wreckingballsoftware.forecastfrenzy.domain

import com.wreckingballsoftware.forecastfrenzy.data.ApiResult
import com.wreckingballsoftware.forecastfrenzy.data.CityRepo
import com.wreckingballsoftware.forecastfrenzy.data.WeatherRepo
import kotlin.math.roundToInt

const val MAX_TIME = 31
const val MAX_ROUNDS = 5
const val BAD_TEMP_VALUE = -1000
const val MINIMUM_TEMP = -130f
const val MAXIMUM_TEMP = 140f

class Gameplay(
    private val cityRepo: CityRepo,
    private val weatherRepo: WeatherRepo,
    private val gameTimer: GameTimer,
) {
    var currentRound = 0
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

    suspend fun getNewCity(onError: (String) -> Unit): String =
        when (val result = cityRepo.getCity(populationFilter[currentRound], orderBy[currentRound])) {
            is ApiResult.Success -> {
                city = result.data
                getCurrentCity()
            }
            is ApiResult.Error -> {
                onError(result.errorMessage)
                ""
            }
    }

    fun startTimer(onTick: () -> Unit, onFinish: () -> Unit) =
        gameTimer.startTimer(onTick, onFinish)

    fun stopTimer() = gameTimer.cancel()

    suspend fun getTemp(onError: (String) -> Unit): Int =
        when (val result =
            weatherRepo.getWeather(lat = city?.lat ?: "", lon = city?.lon ?: "")) {
            is ApiResult.Success -> {
                result.data.toFloat().roundToInt()
            }
            is ApiResult.Error -> {
                onError(result.errorMessage)
                BAD_TEMP_VALUE
            }
        }
}