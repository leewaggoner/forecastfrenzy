package com.wreckingballsoftware.forecastfrenzy.data

import com.wreckingballsoftware.forecastfrenzy.domain.GameCity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.util.Random

class CityRepo(
    private val cityService: CityService,
) {
    suspend fun getCities(filter: String, orderBy: String): ApiResult<GameCity> {
        return callCityApi(filter, orderBy).mapToApiResult()
    }

    private suspend fun callCityApi(filter: String, orderBy: String) = withContext(Dispatchers.IO) {
        try {
            val order = if (orderBy.isNotEmpty()) {
                orderBy + listOf("ASC", "DESC").random()
            } else {
                orderBy
            }
            NetworkResponse.Success(cityService.getCities(filter = filter, orderBy = order))
        } catch (ex: HttpException) {
            ex.toNetworkErrorResponse()
        } catch (ex: Exception) {
            NetworkResponse.Error.UnknownNetworkError(ex)
        }
    }
}

private fun NetworkResponse<CityResponse>.mapToApiResult() : ApiResult<GameCity> =
    when (this) {
        is NetworkResponse.Success -> {
            if (data.message.isNullOrEmpty()) {
                val cityList = data.results.toMutableList()
                cityList.shuffle(Random(System.currentTimeMillis()))
                val city = cityList[0]
                val gameCity = GameCity(
                    name = "${city.name}, ${city.country}",
                    lat = city.latitude,
                    lon = city.longitude,
                )
                ApiResult.Success(gameCity)
            } else {
                ApiResult.Error("Error code ${data.errorCode}: ${data.message}")
            }
        }
        is NetworkResponse.Error -> {
            ApiResult.Error("Error code ${code}: ${exception.localizedMessage}")
        }
    }
