package com.wreckingballsoftware.forecastfrenzy.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class WeatherRepo(private val weatherService: WeatherService) {
    suspend fun getWeather(lat: String, lon: String): ApiResult<String> =
        callWeatherApi(lat, lon).mapToApiResult()

    private suspend fun callWeatherApi(lat: String, lon: String) = withContext(Dispatchers.IO) {
        try {
            NetworkResponse.Success(weatherService.getTemperature(lat, lon))
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
                ApiResult.Success(data.current.temp)
            } else {
                ApiResult.Error("Error code ${data.cod}: ${data.message}")
            }
        }
        is NetworkResponse.Error -> {
            ApiResult.Error("Error code ${code}: ${exception.localizedMessage}")
        }
    }
