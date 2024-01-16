package com.wreckingballsoftware.forecastfrenzy.ui.rules.models

sealed interface RulesEvents {
    data object PlayGame : RulesEvents
}