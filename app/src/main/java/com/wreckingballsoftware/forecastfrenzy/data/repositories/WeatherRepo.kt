package com.wreckingballsoftware.forecastfrenzy.data.repositories

import com.wreckingballsoftware.forecastfrenzy.data.models.NetworkResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.TemperatureResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.toNetworkErrorResponse
import com.wreckingballsoftware.forecastfrenzy.data.storage.WeatherService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class WeatherRepo(private val weatherService: WeatherService) {
    suspend fun getWeather(lat: String, lon: String): NetworkResponse<TemperatureResponse> =
        withContext(Dispatchers.IO) {
            try {
                NetworkResponse.Success(weatherService.getTemperature(lat, lon))
            } catch (ex: HttpException) {
                ex.toNetworkErrorResponse()
            } catch (ex: Exception) {
                NetworkResponse.Error.UnknownNetworkError(ex)
            }
        }
}
