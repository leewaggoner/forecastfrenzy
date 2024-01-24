package com.wreckingballsoftware.forecastfrenzy.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class WeatherRepo(private val weatherService: WeatherService) {
    suspend fun getWeather(lat: String, lon: String): NetworkResponse<TemperatureResponse> =
        callWeatherApi(lat, lon)

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
