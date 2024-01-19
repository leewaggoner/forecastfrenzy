package com.wreckingballsoftware.forecastfrenzy.data

import com.wreckingballsoftware.forecastfrenzy.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("onecall")
    suspend fun getTemperature(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String = "imperial",
        @Query("exclude") exclude: String = "minutely,hourly,daily,alerts",
        @Query("appid") appId: String = BuildConfig.WEATHER_AUTH_KEY
    ): TemperatureResponse
}
