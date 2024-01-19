package com.wreckingballsoftware.forecastfrenzy.data

import retrofit2.http.GET
import retrofit2.http.Query

interface CityService {
    @GET("records")
    suspend fun getCities(
        @Query("select") select: String = "name, country, country_code, population, latitude, longitude",
        @Query("where") filter: String,
        @Query("limit") limit: Int = 100,
    ): CityResponse
}
