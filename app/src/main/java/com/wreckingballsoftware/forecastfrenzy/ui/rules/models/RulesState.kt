package com.wreckingballsoftware.forecastfrenzy.ui.rules.models

import android.os.Parcelable
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.CURRENT_ANTE
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.MAX_ROUNDS
import kotlinx.parcelize.Parcelize

@Parcelize
data class RulesState(
    val numRounds: Int = MAX_ROUNDS,
    val curAnte: Int = CURRENT_ANTE
) : Parcelable