package com.wreckingballsoftware.forecastfrenzy.data.storage

import com.wreckingballsoftware.forecastfrenzy.data.models.AddHighScoreResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.HighScoresResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.UpdateHighScoreResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
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

    @FormUrlEncoded
    @POST("updatehighscore.php")
    suspend fun updateHighScore(
        @Field("id") id: Long,
        @Field("score") score: Int,
    ): UpdateHighScoreResponse

    @FormUrlEncoded
    @POST("addhighscore.php")
    suspend fun addHighScoreEntry(
        @Field("name") name: String,
        @Field("score") score: Int,
    ): AddHighScoreResponse
}