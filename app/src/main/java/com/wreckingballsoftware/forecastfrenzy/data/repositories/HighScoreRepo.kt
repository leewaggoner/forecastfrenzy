package com.wreckingballsoftware.forecastfrenzy.data.repositories

import com.wreckingballsoftware.forecastfrenzy.data.models.AddHighScoreResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.HighScoresResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.LoginRequest
import com.wreckingballsoftware.forecastfrenzy.data.models.NetworkResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.UpdateHighScoreRequest
import com.wreckingballsoftware.forecastfrenzy.data.models.UpdateHighScoreResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.toNetworkErrorResponse
import com.wreckingballsoftware.forecastfrenzy.data.network.HighScoreService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class HighScoreRepo(
    private val highScoreService: HighScoreService,
) {
    suspend fun getHighScores(limit: Int): NetworkResponse<HighScoresResponse> =
        withContext(Dispatchers.IO) {
            try {
                NetworkResponse.Success(highScoreService.getHighScores(limit))
            } catch (ex: HttpException) {
                ex.toNetworkErrorResponse()
            } catch (ex: Exception) {
                NetworkResponse.Error.UnknownNetworkError(ex)
            }
    }

    suspend fun getHighScore(id: Long): NetworkResponse<HighScoresResponse> =
        withContext(Dispatchers.IO) {
            try {
                NetworkResponse.Success(highScoreService.getHighScore(id))
            } catch (ex: HttpException) {
                ex.toNetworkErrorResponse()
            } catch (ex: Exception) {
                NetworkResponse.Error.UnknownNetworkError(ex)
            }
    }

    suspend fun updateHighScore(request: UpdateHighScoreRequest): NetworkResponse<UpdateHighScoreResponse> =
        withContext(Dispatchers.IO) {
            try {
                NetworkResponse.Success(highScoreService.updateHighScore(request))
            } catch (ex: HttpException) {
                ex.toNetworkErrorResponse()
            } catch (ex: Exception) {
                NetworkResponse.Error.UnknownNetworkError(ex)
            }
        }

    suspend fun login(request: LoginRequest): NetworkResponse<AddHighScoreResponse> =
        withContext(Dispatchers.IO) {
            try {
                NetworkResponse.Success(highScoreService.login(request))
            } catch (ex: HttpException) {
                ex.toNetworkErrorResponse()
            } catch (ex: Exception) {
                NetworkResponse.Error.UnknownNetworkError(ex)
            }
        }
}