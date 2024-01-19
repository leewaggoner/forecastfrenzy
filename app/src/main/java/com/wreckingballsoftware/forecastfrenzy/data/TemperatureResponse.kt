package com.wreckingballsoftware.forecastfrenzy.data

data class TemperatureResponse(
    val current: Current,
    val cod: String,
    val message: String? = null,
)

data class Current(
    val temp: String,
)
