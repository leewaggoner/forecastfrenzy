package com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models

import android.os.Parcelable
import com.wreckingballsoftware.forecastfrenzy.domain.MAX_TIME
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.MAX_POINTS
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.ROUND_POINTS
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameplayState(
    val curRound: Int = 0,
    val curAnte: Int = 100,
    val yourPoints: Int = MAX_POINTS,
    val roundPoints: Int = ROUND_POINTS,
    val city: String = "",
    val curGuess: Float = 34f,
    val secondsRemaining: Int = MAX_TIME,
    val answer: Int = 34,
    val errorMessage: String? = null,
) : Parcelable
