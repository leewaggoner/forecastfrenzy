package com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models

import android.os.Parcelable
import com.wreckingballsoftware.forecastfrenzy.domain.CURRENT_ROUND_POINTS
import com.wreckingballsoftware.forecastfrenzy.domain.MAX_TIME
import com.wreckingballsoftware.forecastfrenzy.domain.STARTING_PLAYER_POINTS
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameplayState(
    val curRound: Int = 0,
    val curAnte: Int = 100,
    val curAnteRange: List<String> = listOf(),
    val yourPoints: Int = STARTING_PLAYER_POINTS,
    val roundPoints: Int = CURRENT_ROUND_POINTS,
    val city: String = "",
    val curGuess: Int = 34,
    val secondsRemaining: Int = MAX_TIME,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
) : Parcelable
