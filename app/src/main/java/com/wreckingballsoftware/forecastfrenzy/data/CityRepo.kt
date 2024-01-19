package com.wreckingballsoftware.forecastfrenzy.data

import com.wreckingballsoftware.forecastfrenzy.domain.GameCity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.util.Random

class CityRepo(
    private val cityService: CityService,
) {
    suspend fun getCities(filter: String): ApiResult<List<GameCity>> {
        val result = callCityApi(filter).mapToApiResult()
        return result
    }

    private suspend fun callCityApi(filter: String) = withContext(Dispatchers.IO) {
        try {
            NetworkResponse.Success(cityService.getCities(filter = filter))
        } catch (ex: HttpException) {
            ex.toNetworkErrorResponse()
        } catch (ex: Exception) {
            NetworkResponse.Error.UnknownNetworkError(ex)
        }
    }
}

private fun NetworkResponse<CityResponse>.mapToApiResult() : ApiResult<List<GameCity>> =
    when (this) {
        is NetworkResponse.Success -> {
            if (data.message.isNullOrEmpty()) {
                val mutableList = data.results.toMutableList()
                mutableList.shuffle(Random(System.currentTimeMillis()))
                val cityList = mutableList.take(5)
                val gameCities = mutableListOf<GameCity>()
                for (city in cityList) {
                    gameCities.add(GameCity(name = "${city.name}, ${city.country}", population = city.population))
                }
                ApiResult.Success(gameCities)
            } else {
                ApiResult.Error("Error code ${data.errorCode}: ${data.message}")
            }
        }
        is NetworkResponse.Error -> {
            ApiResult.Error("Error code ${code}: ${exception.localizedMessage}")
        }
    }
