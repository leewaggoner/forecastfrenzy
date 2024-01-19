package com.wreckingballsoftware.forecastfrenzy.ui.gameplay

import android.os.CountDownTimer
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.wreckingballsoftware.forecastfrenzy.data.ApiResult
import com.wreckingballsoftware.forecastfrenzy.data.WeatherRepo
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models.GameplayEvent
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models.GameplayNavigation
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models.GameplayState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

const val MAX_ROUNDS = 5
const val CURRENT_ANTE = 100
const val MAX_POINTS = 1000
const val ROUND_POINTS = 200
const val MAX_TIME = 31
const val MAX_TIMER_TIME = MAX_TIME * 1000L
const val TIMER_INTERVAL = 1000L

class GameplayViewModel(
    handle: SavedStateHandle,
    private val weatherRepo: WeatherRepo
) : ViewModel() {
    @OptIn(SavedStateHandleSaveableApi::class)
    var state by handle.saveable {
        mutableStateOf(GameplayState())
    }
    val navigation = MutableSharedFlow<GameplayNavigation>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )
    private val gameTimer = object : CountDownTimer(
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
        viewModelScope.launch(Dispatchers.Main) {
            when (val result = weatherRepo.getWeather("San Diego")) {
                is ApiResult.Loading -> { }
                is ApiResult.Success -> state = state.copy(answer = result.data?.toInt() ?: -150)
                is ApiResult.Error -> Log.e("-----LEE-----", result.message)
            }
        }
    }

    fun handleEvent(event: GameplayEvent) {
        when (event) {
            is GameplayEvent.GuessChanged -> {
                state = state.copy(curGuess = event.temperature.roundToInt().toFloat())
            }
            GameplayEvent.DisplayResults -> {
                viewModelScope.launch(Dispatchers.Main) {
                    navigation.emit(GameplayNavigation.ViewResults)
                }
            }
            is GameplayEvent.StartGame -> {
                gameTimer.start()
            }
        }
    }
}