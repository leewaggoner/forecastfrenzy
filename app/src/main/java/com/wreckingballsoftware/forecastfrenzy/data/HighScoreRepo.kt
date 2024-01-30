package com.wreckingballsoftware.forecastfrenzy.data

import com.wreckingballsoftware.forecastfrenzy.data.models.AddHighScoreResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.HighScoresResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.NetworkResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.UpdateHighScoreResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.toNetworkErrorResponse
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
                val result = highScoreService.getHighScore(id)
                NetworkResponse.Success(result)
            } catch (ex: HttpException) {
                ex.toNetworkErrorResponse()
            } catch (ex: Exception) {
                NetworkResponse.Error.UnknownNetworkError(ex)
            }
    }

    suspend fun updateHighScore(id: Long, score: Int): NetworkResponse<UpdateHighScoreResponse> =
        withContext(Dispatchers.IO) {
            try {
                NetworkResponse.Success(highScoreService.updateHighScore(id = id, score = score))
            } catch (ex: HttpException) {
                ex.toNetworkErrorResponse()
            } catch (ex: Exception) {
                NetworkResponse.Error.UnknownNetworkError(ex)
            }
        }

    suspend fun addHighScore(name: String, score: Int): NetworkResponse<AddHighScoreResponse> =
        withContext(Dispatchers.IO) {
            try {
                NetworkResponse.Success(highScoreService.addHighScore(name = name, score = score))
            } catch (ex: HttpException) {
                ex.toNetworkErrorResponse()
            } catch (ex: Exception) {
                NetworkResponse.Error.UnknownNetworkError(ex)
            }
        }
}