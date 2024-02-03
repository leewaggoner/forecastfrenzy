package com.wreckingballsoftware.forecastfrenzy.domain

import com.wreckingballsoftware.forecastfrenzy.data.models.AddHighScoreEntryRequest
import com.wreckingballsoftware.forecastfrenzy.data.models.AddHighScoreResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.ApiResult
import com.wreckingballsoftware.forecastfrenzy.data.models.HighScoresResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.NetworkResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.UpdateHighScoreRequest
import com.wreckingballsoftware.forecastfrenzy.data.models.UpdateHighScoreResponse
import com.wreckingballsoftware.forecastfrenzy.data.repositories.HighScoreRepo
import com.wreckingballsoftware.forecastfrenzy.domain.models.HighScore
import kotlin.math.abs
import kotlin.math.roundToInt

const val STARTING_PLAYER_POINTS = 1000
const val HIGHSCORE_LIMIT = 5

class GameScore(
    private val highScoreRepo: HighScoreRepo,
) {
    private val roundPoints = listOf(
        200,
        400,
        600,
        800,
        1000,
    )
    private val antePoints = listOf(
        100,
        200,
        300,
        400,
        500,
    )
    private var currentAntePoints = antePoints[0]
    var roundMaxPoints = roundPoints[0]
        private set
    var currentScore = STARTING_PLAYER_POINTS
        private set
    var roundScore = 0
        private set
    var timeBonus = 0
        private set
    private var highScores = listOf<HighScore>()
    private var highScore: HighScore? = null

    fun advanceRound(currentRound: Int) {
        roundMaxPoints = roundPoints[currentRound]
        currentAntePoints = antePoints[currentRound]
        roundScore = 0
        timeBonus = 0
    }

    suspend fun startNewGame(onError: (String) -> Unit) {
        currentScore = STARTING_PLAYER_POINTS
        roundMaxPoints = roundPoints[0]
        currentAntePoints = antePoints[0]
        roundScore = 0
        timeBonus = 0
//        updateHighScore(UpdateHighScoreRequest(id = 2, score = 1010), onError)
//        getHighScore(onError)
//        getHighScores(onError)
//        addHighScoreEntry(AddHighScoreEntryRequest("Lee"), onError)
    }

    /**
     * Store initial high score data
     */
    private suspend fun addHighScoreEntry(
        request: AddHighScoreEntryRequest,
        onError: (String) -> Unit
    ) {
        when (val result = highScoreRepo.addHighScoreEntry(request).mapToAdd()) {
            is ApiResult.Success -> {
                val id = result.data
            }
            is ApiResult.Error -> {
                onError(result.errorMessage)
            }
        }
    }

    private suspend fun updateHighScore(
        request: UpdateHighScoreRequest,
        onError: (String) -> Unit
    ) {
        when (val result = highScoreRepo.updateHighScore(request).mapToUpdate()) {
            is ApiResult.Success -> {
                val ok = result.data
                if (!ok) {
                    onError("Unable to update score.")
                }
            }
            is ApiResult.Error -> {
                onError(result.errorMessage)
            }
        }
    }

    private suspend fun getHighScore(onError: (String) -> Unit) {
        when (val result = highScoreRepo.getHighScore(3L).mapToHighScore()) {
            is ApiResult.Success -> {
                highScore = result.data
            }
            is ApiResult.Error -> {
                onError(result.errorMessage)
            }
        }
    }

    private suspend fun getHighScores(onError: (String) -> Unit) {
        when (val result = highScoreRepo.getHighScores(HIGHSCORE_LIMIT).mapToHighScoreList()) {
            is ApiResult.Success -> {
                highScores =  result.data
            }
            is ApiResult.Error -> {
                onError(result.errorMessage)
            }
        }
    }

    fun getCurrentAntePoints(): List<String> =
        (currentAntePoints..roundMaxPoints step 10).map { it.toString() }

    fun handleGuess(guess: Int, actualTemp: Int, bet: Int, seconds: Int) {
        val absGuess = abs(actualTemp - guess)
        roundScore = when (absGuess) {
            0 -> {
                //perfect guess
                timeBonus = (MAX_TIME - seconds) * 5
                bet * 2
            }
            in 1..4 -> {
                //guess is within 5 of the actual temp
                timeBonus = (MAX_TIME - seconds) * 5
                val percent = ((5f - absGuess) * 2f) / 10f
                (bet * percent).roundToInt()
            }
            5 -> {
                //guess is 5 away from the actual temperature
                timeBonus = (MAX_TIME - seconds) * 5
                10
            }
            in 6..9 -> {
                //guess is more than 5, but within 10 of the actual temperature
                val over5Guess = abs(5 - absGuess)
                val percent = (over5Guess * 2f) / 10f
                val points = bet.toFloat() * percent
                -points.roundToInt()
            }
            else -> {
                //guess is 10 or more away from the actual temperature
                -bet
            }
        }
        currentScore += roundScore + timeBonus
    }
}

private fun NetworkResponse<HighScoresResponse>.mapToHighScoreList(): ApiResult<List<HighScore>> =
    when (this) {
        is NetworkResponse.Success -> {
            if (data.error == null) {
                val highScores = mutableListOf<HighScore>()
                for (score in data.scores) {
                    highScores.add(HighScore(name = score.name, score = score.score))
                }
                ApiResult.Success(highScores)
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

private fun NetworkResponse<HighScoresResponse>.mapToHighScore(): ApiResult<HighScore> =
    when (this) {
        is NetworkResponse.Success -> {
            if (data.error == null) {
                if (data.scores.isNotEmpty()) {
                    ApiResult.Success(HighScore(name = data.scores[0].name, score = data.scores[0].score))
                } else {
                    ApiResult.Error("Unknown network error.")
                }
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

private fun NetworkResponse<UpdateHighScoreResponse>.mapToUpdate(): ApiResult<Boolean> =
    when (this) {
        is NetworkResponse.Success -> {
            if (data.error == null) {
                ApiResult.Success(data.success)
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


private fun NetworkResponse<AddHighScoreResponse>.mapToAdd(): ApiResult<Long> =
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
