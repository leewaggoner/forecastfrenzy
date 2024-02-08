package com.wreckingballsoftware.forecastfrenzy.domain

import com.wreckingballsoftware.forecastfrenzy.data.models.AddHighScoreResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.LoginRequest
import com.wreckingballsoftware.forecastfrenzy.data.models.NetworkResponse
import com.wreckingballsoftware.forecastfrenzy.data.repositories.HighScoreRepo
import com.wreckingballsoftware.forecastfrenzy.data.storage.DataStoreWrapper
import com.wreckingballsoftware.forecastfrenzy.domain.models.ApiResult

class Login(
    private val highScoreRepo: HighScoreRepo,
    private val dataStoreWrapper: DataStoreWrapper,
) {
    suspend fun isLoggedIn(): Boolean = dataStoreWrapper.getPlayerId(0L) != 0L

    suspend fun loginPlayer(
        request: LoginRequest,
        onError: (String) -> Unit
    ): Boolean {
        return if (dataStoreWrapper.getPlayerId(0L) == 0L) {
            when (val result = highScoreRepo.login(request).mapToLogin()) {
                is ApiResult.Success -> {
                    val id = result.data
                    with(dataStoreWrapper) {
                        putPlayerId(id)
                        putPlayerName(request.name)
                        putPlayerEmail(request.email)
                        putPlayerHighScore(0)
                    }
                    true
                }

                is ApiResult.Error -> {
                    onError(result.errorMessage)
                    false
                }
            }
        } else {
            true
        }
    }

    suspend fun deletePlayerData() {
        dataStoreWrapper.clearAll()
    }
}

private fun NetworkResponse<AddHighScoreResponse>.mapToLogin(): ApiResult<Long> =
    when (this) {
        is NetworkResponse.Success -> {
            if (data.error == null) {
                ApiResult.Success(data.id)
            } else {
                ApiResult.Error(
                    errorMessage = "Error code ${data.error.errorCode}: ${data.error.errorMessage}"
                )
            }
        }
        is NetworkResponse.Error -> {
            ApiResult.Error(errorMessage = "Error code ${code}: ${exception.localizedMessage}")
        }
    }
