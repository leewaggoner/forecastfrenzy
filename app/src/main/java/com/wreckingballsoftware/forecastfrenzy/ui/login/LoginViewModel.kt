package com.wreckingballsoftware.forecastfrenzy.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.wreckingballsoftware.forecastfrenzy.domain.Login
import com.wreckingballsoftware.forecastfrenzy.ui.login.models.LoginNavigation
import com.wreckingballsoftware.forecastfrenzy.ui.login.models.LoginState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

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

    fun onNameChanged(name: String) {
        state = state.copy(name = name)
    }

    fun onEmailChanged(email: String) {
        state = state.copy(email = email)
    }

    fun onLogin() {
        viewModelScope.launch(Dispatchers.Main) {
            navigation.emit(LoginNavigation.GoToRulesScreen)
        }
    }
}