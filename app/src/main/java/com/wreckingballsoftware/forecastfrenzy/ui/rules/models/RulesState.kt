package com.wreckingballsoftware.forecastfrenzy.ui.rules.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RulesState(
    val numRounds: Int = 5
) : Parcelable