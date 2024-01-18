package com.wreckingballsoftware.forecastfrenzy.data

import com.wreckingballsoftware.forecastfrenzy.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("weather")
    suspend fun getTemperature(
        @Query("q") location: String,
        @Query("units") units: String = "imperial",
        @Query("APPID") appId: String = BuildConfig.WEATHER_AUTH_KEY
    ): TemperatureResponse
}
