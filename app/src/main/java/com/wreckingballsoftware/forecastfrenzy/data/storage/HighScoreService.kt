package com.wreckingballsoftware.forecastfrenzy.data.storage

import com.wreckingballsoftware.forecastfrenzy.data.models.AddHighScoreEntryRequest
import com.wreckingballsoftware.forecastfrenzy.data.models.AddHighScoreResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.HighScoresResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.UpdateHighScoreRequest
import com.wreckingballsoftware.forecastfrenzy.data.models.UpdateHighScoreResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface HighScoreService {
    @GET("highscores.php")
    suspend fun getHighScores(
        @Query("limit") limit: Int,
    ): HighScoresResponse

    @GET("highscore.php")
    suspend fun getHighScore(
        @Query("id") id: Long,
    ): HighScoresResponse

    @POST("updatehighscore.php")
    suspend fun updateHighScore(
        @Body request: UpdateHighScoreRequest,
    ): UpdateHighScoreResponse

    @POST("addhighscore.php")
    suspend fun addHighScoreEntry(
        @Body request: AddHighScoreEntryRequest,
    ): AddHighScoreResponse
}