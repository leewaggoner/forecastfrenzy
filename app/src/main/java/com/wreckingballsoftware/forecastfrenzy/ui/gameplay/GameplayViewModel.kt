package com.wreckingballsoftware.forecastfrenzy.ui.gameplay

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.wreckingballsoftware.forecastfrenzy.domain.GameScore
import com.wreckingballsoftware.forecastfrenzy.domain.Gameplay
import com.wreckingballsoftware.forecastfrenzy.domain.MAX_TIME
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models.GameplayEvent
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models.GameplayNavigation
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models.GameplayState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class GameplayViewModel(
    handle: SavedStateHandle,
    private val gameplay: Gameplay,
    private val gameScore: GameScore,
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
        startRound()
    }

    private fun startRound() {
        initPlayerInfo()
        viewModelScope.launch(Dispatchers.Main) {
            handleEvent(GameplayEvent.Loading(isLoading = true))
            val city = getCity()
            handleEvent(GameplayEvent.StartRound(city = city))
            handleEvent(GameplayEvent.Loading(isLoading = false))
        }
    }

    private suspend fun getCity(): String {
        gameplay.startNewRound()
        val city = gameplay.getCurrentCity()
        return city.ifEmpty {
            handleEvent(GameplayEvent.ApiError(message = "Unknown error."))
            ""
        }
    }

    private fun initPlayerInfo() {
        handleEvent(
            GameplayEvent.InitRound(
                curRound = gameplay.currentRound,
                playerPoints = gameScore.currentScore,
                roundPoints = gameScore.roundMaxPoints,
                antePoints = gameScore.getCurrentAntePoints(),
            )
        )
    }

    fun handleEvent(event: GameplayEvent) {
        when (event) {
            //initialize game round
            is GameplayEvent.InitRound -> {
                state = state.copy(
                    curRound = event.curRound,
                    yourPoints = event.playerPoints,
                    roundPoints = event.roundPoints,
                    curAnteRange = event.antePoints,
                )
            }
            //show or hide progress indicator
            is GameplayEvent.Loading -> {
                state = state.copy(isLoading = event.isLoading)
            }
            //player changed their temperature guess
            is GameplayEvent.GuessChanged -> {
                state = state.copy(curGuess = event.temperature.roundToInt())
            }
            is GameplayEvent.AnteChanged -> {
                state = state.copy(curAnte = event.newAnte.toInt())
            }
            //start the game round
            is GameplayEvent.StartRound -> {
                state = state.copy(city = event.city)
                //start the gameplay timer
                gameplay.startTimer(
                    onTick = {
                        state = state.copy(secondsRemaining = state.secondsRemaining - 1)
                    },
                    onFinish = {
                        //out of time -- go to the results screen
                        state = state.copy(secondsRemaining = 0)
                        handleEvent(GameplayEvent.DisplayResults)
                    }
                )
            }
            //display the results screen
            GameplayEvent.DisplayResults -> {
                viewModelScope.launch(Dispatchers.Main) {
                    gameplay.stopTimer()
                    navigation.emit(
                        GameplayNavigation.ViewResults(
                            guess = state.curGuess,
                            bet = state.curAnte,
                            seconds = MAX_TIME - state.secondsRemaining,
                        )
                    )
                }
            }
            //display the error dialog
            is GameplayEvent.ApiError -> {
                state = state.copy(errorMessage = event.message)
            }
            //dismiss the error dialog
            GameplayEvent.DismissErrorDialog -> {
                state = state.copy(errorMessage = null)
            }
        }
    }
}