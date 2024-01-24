package com.wreckingballsoftware.forecastfrenzy.domain

import com.wreckingballsoftware.forecastfrenzy.data.ApiResult
import com.wreckingballsoftware.forecastfrenzy.data.CityRepo
import com.wreckingballsoftware.forecastfrenzy.data.CityResponse
import com.wreckingballsoftware.forecastfrenzy.data.NetworkResponse
import com.wreckingballsoftware.forecastfrenzy.data.TemperatureResponse
import com.wreckingballsoftware.forecastfrenzy.data.WeatherRepo
import com.wreckingballsoftware.forecastfrenzy.domain.models.GameCity
import kotlin.math.roundToInt
import kotlin.random.Random

const val MAX_TIME = 31 // it'll display 30
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
    private var city: GameCity = GameCity()
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

    private fun getOrderBy(): String {
        var order = orderBy[currentRound]
        if (order.isNotEmpty()) {
            order += listOf("ASC", "DESC").random(Random(System.currentTimeMillis()))
        }
        return order
    }

    fun isGameOver() = currentRound == MAX_ROUNDS - 1

    fun advanceRound() {
        if (!isGameOver()) {
            currentRound++
        }
    }

    fun startNewGame() {
        currentRound = 0
    }

    suspend fun getNewCity(onError: (String) -> Unit): String {
        var cityName = ""
        val result = cityRepo.getCity(populationFilter[currentRound], getOrderBy())
            .mapToGameCity()
        when (result) {
            is ApiResult.Success -> {
                city = result.data
                cityName = city.name
            }

            is ApiResult.Error -> {
                onError(result.errorMessage)
            }
        }
        return cityName
    }

    fun startTimer(onTick: () -> Unit, onFinish: () -> Unit) =
        gameTimer.startTimer(onTick, onFinish)

    fun stopTimer() = gameTimer.cancel()

    suspend fun getTemp(onError: (String) -> Unit): Int {
        var temp = BAD_TEMP_VALUE
        val result = weatherRepo.getWeather(lat = city.lat, lon = city.lon)
            .mapToTemperatureString()
        when (result) {
            is ApiResult.Success -> {
                temp = result.data.toFloat().roundToInt()
            }

            is ApiResult.Error -> {
                onError(result.errorMessage)
            }
        }
        return temp
    }
}


private fun NetworkResponse<CityResponse>.mapToGameCity() : ApiResult<GameCity> =
    when (this) {
        is NetworkResponse.Success -> {
            if (data.message.isNullOrEmpty()) {
                val city = data.results.random(Random(System.currentTimeMillis()))
                val gameCity = GameCity(
                    name = "${city.name}, ${city.country}",
                    lat = city.latitude,
                    lon = city.longitude,
                )
                ApiResult.Success(gameCity)
            } else {
                ApiResult.Error("Error code ${data.errorCode}: ${data.message}")
            }
        }
        is NetworkResponse.Error -> {
            ApiResult.Error("Error code ${code}: ${exception.localizedMessage}")
        }
    }

private fun NetworkResponse<TemperatureResponse>.mapToTemperatureString() : ApiResult<String> =
    when (this) {
        is NetworkResponse.Success -> {
            if (data.message.isNullOrEmpty()) {
                ApiResult.Success(data.current.temp)
            } else {
                ApiResult.Error("Error code ${data.cod}: ${data.message}")
            }
        }
        is NetworkResponse.Error -> {
            ApiResult.Error("Error code ${code}: ${exception.localizedMessage}")
        }
    }

