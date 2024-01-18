package com.wreckingballsoftware.forecastfrenzy.data

data class TemperatureResponse(
    val name: String,
    val main: MainResponse,
    val cod: String?,
    val message: String?,
)

data class MainResponse(
    val temp: String
)