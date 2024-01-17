package com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models

import android.os.Parcelable
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.MAX_POINTS
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.MAX_ROUNDS
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.MAX_TIME
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.ROUND_POINTS
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameplayState(
    val curRound: Int = 0,
    val curAnte: Int = 100,
    val yourPoints: Int = MAX_POINTS,
    val roundPoints: Int = ROUND_POINTS,
    val cities: List<String> = List(size = MAX_ROUNDS) { "San Diego" },
    val guess: String = "",
    val secondsRemaining: Int = MAX_TIME,
) : Parcelable
