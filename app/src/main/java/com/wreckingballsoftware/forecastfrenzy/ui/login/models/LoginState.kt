package com.wreckingballsoftware.forecastfrenzy.ui.login.models

data class LoginState(
    val name: String = "",
    val email: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
