package com.wreckingballsoftware.forecastfrenzy.ui.gameplay

import android.os.CountDownTimer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models.GameplayNavigation
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models.GameplayState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

const val MAX_ROUNDS = 5
const val CURRENT_ANTE = 100
const val MAX_POINTS = 1000
const val ROUND_POINTS = 200
const val MAX_TIME = 16
const val MAX_TIMER_TIME = MAX_TIME * 1000L
const val TIMER_INTERVAL = 1000L

class GameplayViewModel(
    handle: SavedStateHandle,
) : ViewModel() {
    @OptIn(SavedStateHandleSaveableApi::class)
    var state by handle.saveable {
        mutableStateOf(GameplayState())
    }
    val navigation = MutableSharedFlow<GameplayNavigation>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )
    val gameTimer = object : CountDownTimer(
        MAX_TIMER_TIME,
        TIMER_INTERVAL,
    ) {
        override fun onTick(millisUntilFinished: Long) {
            state = state.copy(secondsRemaining = state.secondsRemaining - 1)
        }

        override fun onFinish() {
            state = state.copy(secondsRemaining = 0)
        }
    }

    init {
        gameTimer.start()
    }

    fun onTextChanged(temperature: String) {
        val pattern  = Regex("^[-+]?\\d*$")
        if(temperature.matches(pattern)) {
            state = state.copy(guess = temperature)
        }
    }

    fun onDisplayResults() {
        viewModelScope.launch(Dispatchers.Main) {
            navigation.emit(GameplayNavigation.ViewResults)
        }
    }
}