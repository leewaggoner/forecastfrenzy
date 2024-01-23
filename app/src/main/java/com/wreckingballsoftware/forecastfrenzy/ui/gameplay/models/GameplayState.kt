package com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models

import android.os.Parcelable
import com.wreckingballsoftware.forecastfrenzy.domain.MAX_TIME
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameplayState(
    val curRound: Int = 0,
    val curAnte: Int = 0,
    val curAnteRange: List<String> = listOf(),
    val yourPoints: Int = 0,
    val roundPoints: Int = 0,
    val city: String = "",
    val curGuess: Int = 32,
    val secondsRemaining: Int = MAX_TIME,
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
) : Parcelable
