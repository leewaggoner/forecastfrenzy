package com.wreckingballsoftware.forecastfrenzy.data.models

import com.google.gson.annotations.SerializedName

data class CityResponse(
    val results: List<CityResult>,
    @SerializedName("error_code")
    val errorCode: String,
    val message: String? = null,
)

data class CityResult(
    val name: String,
    val country: String,
    @SerializedName("country_code")
    val countryCode: String,
    val population: Int,
    val latitude: String,
    val longitude: String,
)