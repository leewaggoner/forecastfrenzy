package com.wreckingballsoftware.forecastfrenzy.ui.gameplay

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.wreckingballsoftware.forecastfrenzy.data.ApiResult
import com.wreckingballsoftware.forecastfrenzy.data.CityRepo
import com.wreckingballsoftware.forecastfrenzy.data.WeatherRepo
import com.wreckingballsoftware.forecastfrenzy.domain.GameTimer
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

class GameplayViewModel(
    handle: SavedStateHandle,
    private val gameTimer: GameTimer,
    private val weatherRepo: WeatherRepo,
    private val cityRepo: CityRepo,
) : ViewModel() {
    @OptIn(SavedStateHandleSaveableApi::class)
    var state by handle.saveable {
        mutableStateOf(GameplayState())
    }
    val navigation = MutableSharedFlow<GameplayNavigation>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )

    init {
        viewModelScope.launch(Dispatchers.Main) {
            when (val result = weatherRepo.getWeather(lat = "45.1275", lon = "11.5433")) {
                is ApiResult.Loading -> { }
                is ApiResult.Success -> {
                    val temp = result.data?.toFloat()?.roundToInt() ?: -150
                }
                is ApiResult.Error -> {
                    handleEvent(GameplayEvent.ApiError(result.message))
                }
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            when (val result = cityRepo.getCities("population > 10000000")) {
                is ApiResult.Loading -> { }
                is ApiResult.Success -> {
                    val city = result.data?.get(0)?.name
                    handleEvent(
                        GameplayEvent.StartGame(
                            city = city ?: "San Diego",
                            temperature = -150
                        )
                    )
                }
                is ApiResult.Error -> {
                    handleEvent(GameplayEvent.ApiError(result.message))
                }
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
                state = state.copy(city = event.city, answer = event.temperature)
                gameTimer.startTimer(
                    onTick = {
                        state = state.copy(secondsRemaining = state.secondsRemaining - 1)
                    },
                    onFinish = {
                        state = state.copy(secondsRemaining = 0)
                        handleEvent(GameplayEvent.DisplayResults)
                    }
                )
            }
            is GameplayEvent.ApiError -> {
                state = state.copy(errorMessage = event.message)
            }
            GameplayEvent.DismissErrorDialog -> {
                state = state.copy(errorMessage = null)
            }
        }
    }
}