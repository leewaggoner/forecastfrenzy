package com.wreckingballsoftware.forecastfrenzy.ui.login.models

sealed interface LoginNavigation {
    data object GoToRulesScreen : LoginNavigation
}