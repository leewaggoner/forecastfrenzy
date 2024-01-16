package com.wreckingballsoftware.forecastfrenzy.ui.rules.models

sealed interface RulesNavigation {
    data object StartGame : RulesNavigation
}