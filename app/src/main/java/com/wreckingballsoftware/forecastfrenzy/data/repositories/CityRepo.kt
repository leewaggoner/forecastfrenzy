package com.wreckingballsoftware.forecastfrenzy.data.repositories

import com.wreckingballsoftware.forecastfrenzy.data.models.CityResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.NetworkResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.toNetworkErrorResponse
import com.wreckingballsoftware.forecastfrenzy.data.storage.CityService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class CityRepo(
    private val cityService: CityService,
) {
    suspend fun getCity(filter: String, orderBy: String): NetworkResponse<CityResponse> =
        withContext(Dispatchers.IO) {
            try {
                NetworkResponse.Success(cityService.getCities(filter = filter, orderBy = orderBy))
            } catch (ex: HttpException) {
                ex.toNetworkErrorResponse()
            } catch (ex: Exception) {
                NetworkResponse.Error.UnknownNetworkError(ex)
            }
    }
}
