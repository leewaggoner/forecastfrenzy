package com.wreckingballsoftware.forecastfrenzy.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class WeatherRepo(private val weatherService: WeatherService) {
    suspend fun getWeather(location: String): ApiResult<String> {
        val result = callWeatherApi(location).mapToApiResult()
        return result
    }

    private suspend fun callWeatherApi(location: String) = withContext(Dispatchers.IO) {
        try {
            NetworkResponse.Success(weatherService.getTemperature(location))
        } catch (ex: HttpException) {
            ex.toNetworkErrorResponse()
        } catch (ex: Exception) {
            NetworkResponse.Error.UnknownNetworkError(ex)
        }
    }
}

private fun NetworkResponse<TemperatureResponse>.mapToApiResult() : ApiResult<String> =
    when (this) {
        is NetworkResponse.Success -> {
            if (data.message.isNullOrEmpty()) {
                ApiResult.Success(data.main.temp)
            } else {
                ApiResult.Error("Error code ${data.cod}: ${data.message}")
            }
        }
        is NetworkResponse.Error -> {
            ApiResult.Error("Error code ${code}: ${exception.localizedMessage}")
        }
    }
