package com.wreckingballsoftware.forecastfrenzy.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class CityRepo(
    private val cityService: CityService,
) {
    suspend fun getCity(filter: String, orderBy: String): NetworkResponse<CityResponse> {
        return callCityApi(filter, orderBy)
    }

    private suspend fun callCityApi(filter: String, orderBy: String) = withContext(Dispatchers.IO) {
        try {
            NetworkResponse.Success(cityService.getCities(filter = filter, orderBy = orderBy))
        } catch (ex: HttpException) {
            ex.toNetworkErrorResponse()
        } catch (ex: Exception) {
            NetworkResponse.Error.UnknownNetworkError(ex)
        }
    }
}
