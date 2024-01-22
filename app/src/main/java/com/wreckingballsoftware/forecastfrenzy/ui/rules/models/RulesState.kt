package com.wreckingballsoftware.forecastfrenzy.ui.rules.models

import android.os.Parcelable
import com.wreckingballsoftware.forecastfrenzy.domain.CURRENT_ANTE
import com.wreckingballsoftware.forecastfrenzy.domain.MAX_ROUNDS
import kotlinx.parcelize.Parcelize

@Parcelize
data class RulesState(
    val numRounds: Int = MAX_ROUNDS,
    val curAnte: Int = CURRENT_ANTE,
) : Parcelable