package com.wreckingballsoftware.forecastfrenzy.ui.login.models

data class LoginState(
    val name: String = "",
    val email: String = "",
    val invalidNameId: Int = 0,
    val invalidEmailId: Int = 0,
    val loginEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val apiErrorMessage: String? = null,
)
