package com.wreckingballsoftware.forecastfrenzy.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.wreckingballsoftware.forecastfrenzy.R
import com.wreckingballsoftware.forecastfrenzy.data.models.LoginRequest
import com.wreckingballsoftware.forecastfrenzy.domain.Login
import com.wreckingballsoftware.forecastfrenzy.ui.login.models.LoginEvent
import com.wreckingballsoftware.forecastfrenzy.ui.login.models.LoginNavigation
import com.wreckingballsoftware.forecastfrenzy.ui.login.models.LoginState
import com.wreckingballsoftware.forecastfrenzy.utils.isValidEmail
import com.wreckingballsoftware.forecastfrenzy.utils.isValidNameCharacters
import com.wreckingballsoftware.forecastfrenzy.utils.isValidNameLength
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

const val MAX_NAME_LENGTH = 10

class LoginViewModel(
    handle: SavedStateHandle,
    private val login: Login,
) : ViewModel() {
    @OptIn(SavedStateHandleSaveableApi::class)
    var state by handle.saveable {
        mutableStateOf(LoginState())
    }
    val navigation = MutableSharedFlow<LoginNavigation>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )

    fun handleEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.UpdateName -> {
                if (event.name.length <= MAX_NAME_LENGTH) {
                    state = state.copy(
                        name = event.name,
                        loginEnabled = enableLoginButton(name = event.name, email = state.email),
                        invalidNameId = 0
                    )
                }
            }
            is LoginEvent.UpdateEmail -> {
                state = state.copy(
                    email = event.email,
                    loginEnabled = enableLoginButton(name = state.name, email = event.email),
                    invalidEmailId = 0
                )
            }
            LoginEvent.LoginButtonPressed -> {
                onLogin()
            }
            is LoginEvent.InvalidName -> {
                state = state.copy(invalidNameId = event.messageId)
            }
            is LoginEvent.InvalidEmail -> {
                state = state.copy(invalidEmailId = event.messageId)
            }
            LoginEvent.DismissAlert -> {
                state = state.copy(apiErrorMessage = null)
            }
        }
    }

    private fun onLogin() {
        state = state.copy(invalidNameId = 0, invalidEmailId = 0)
        if (isValidName(state.name) && isValidEmail(state.email)) {
            viewModelScope.launch(Dispatchers.Main) {
                state = state.copy(isLoading = true)
                val loggedIn = login.loginPlayer(
                    LoginRequest(name = state.name, email = state.email)
                ) { message ->
                    state = state.copy(apiErrorMessage = message)
                }
                state = state.copy(isLoading = false)
                if (loggedIn) {
                    navigation.emit(LoginNavigation.GoToRulesScreen)
                }
            }
        }
    }

    private fun enableLoginButton(name: String, email: String): Boolean {
        return (name.isNotEmpty() && email.isNotEmpty())
    }

    private fun isValidName(name: String): Boolean {
        var result = true
        if (!name.isValidNameLength()) {
            handleEvent(LoginEvent.InvalidName(R.string.empty_name))
            result = false
        }
        if (!name.isValidNameCharacters()) {
            handleEvent(LoginEvent.InvalidName(R.string.invalid_name))
            result = false
        }
        return result
    }

    private fun isValidEmail(email: String): Boolean {
        var result = true
        if (email.isEmpty()) {
            handleEvent(LoginEvent.InvalidEmail(R.string.empty_email))
            result = false
        }
        if (!email.isValidEmail()) {
            handleEvent(LoginEvent.InvalidEmail(R.string.invalid_email))
            result = false
        }
        return result
    }
}