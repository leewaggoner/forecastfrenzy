package com.wreckingballsoftware.forecastfrenzy.ui.login.models

sealed interface LoginEvent {
    data class UpdateName(val name: String) : LoginEvent
    data class UpdateEmail(val email: String) : LoginEvent
    data object LoginButtonPressed : LoginEvent
    data class InvalidName(val messageId: Int) : LoginEvent
    data class InvalidEmail(val messageId: Int) : LoginEvent
    data object DismissAlert : LoginEvent
}