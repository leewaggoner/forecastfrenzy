package com.wreckingballsoftware.forecastfrenzy.data.models

data class LoginRequest(
    val name: String,
    val email: String,
    val score: Int = 0,
)
