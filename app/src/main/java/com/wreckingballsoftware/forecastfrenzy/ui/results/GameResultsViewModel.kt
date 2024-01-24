package com.wreckingballsoftware.forecastfrenzy.ui.results

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.wreckingballsoftware.forecastfrenzy.R
import com.wreckingballsoftware.forecastfrenzy.domain.BAD_TEMP_VALUE
import com.wreckingballsoftware.forecastfrenzy.domain.GameScore
import com.wreckingballsoftware.forecastfrenzy.domain.Gameplay
import com.wreckingballsoftware.forecastfrenzy.ui.results.models.GameResultsEvent
import com.wreckingballsoftware.forecastfrenzy.ui.results.models.GameResultsNavigation
import com.wreckingballsoftware.forecastfrenzy.ui.results.models.GameResultsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class GameResultsViewModel(
    handle: SavedStateHandle,
    private val gameplay: Gameplay,
    private val gameScore: GameScore,
    private val cityName: String,
    private val guess: Int,
    private val bet: Int,
    private val seconds: Int,
) : ViewModel() {
    @OptIn(SavedStateHandleSaveableApi::class)
    var state by handle.saveable {
        mutableStateOf(GameResultsState())
    }
    val navigation = MutableSharedFlow<GameResultsNavigation>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )

    init {
        initGameInfo()
        getCurrentTemp()
    }

    private fun getCurrentTemp() {
        viewModelScope.launch {
            handleEvent(GameResultsEvent.Loading(isLoading = true))
            val temp = gameplay.getTemp { errorMessage ->
                handleEvent(GameResultsEvent.ApiError(errorMessage))
            }
            if (temp != BAD_TEMP_VALUE) {
                handleEvent(GameResultsEvent.HandleGuess(temp))
            }
            handleEvent(GameResultsEvent.Loading(isLoading = false))
        }
    }

    private fun initGameInfo() {
        if (gameplay.isGameOver()) {
            handleEvent(GameResultsEvent.GameOver)
        }
        handleEvent(
            GameResultsEvent.InitResults(
                buttonText = if (state.isGameOver) R.string.new_game else R.string.next_round,
                headlineText = if (state.isGameOver) R.string.game_results else R.string.round_results,
                currentRound = gameplay.currentRound + 1,
            )
        )
    }

    fun handleEvent(event: GameResultsEvent) {
        when (event) {
            is GameResultsEvent.Loading -> {
                state = state.copy(isLoading = event.isLoading)
            }
            is GameResultsEvent.InitResults -> {
                state = state.copy(
                    buttonTextId = event.buttonText,
                    headlineTextId = event.headlineText,
                    currentRound = event.currentRound,
                )
            }
            is GameResultsEvent.HandleGuess -> {
                gameScore.handleGuess(
                    guess = guess,
                    actualTemp = event.actualTemperature,
                    bet = bet,
                    seconds = seconds,
                )
                state = state.copy(
                    cityName = cityName,
                    guess = guess,
                    actualTemp = event.actualTemperature,
                    totalScore = gameScore.currentScore,
                    roundScore = gameScore.roundScore,
                    timeBonus = gameScore.timeBonus,
                    roundMaxPoints = gameScore.roundMaxPoints,
                    bet = bet,
                )
            }
            is GameResultsEvent.ApiError -> {
                state = state.copy(errorMessage = event.message)
            }
            GameResultsEvent.DismissErrorDialog -> {
                state = state.copy(errorMessage = null)
            }
            GameResultsEvent.StartNextRound -> {
                viewModelScope.launch(Dispatchers.Main) {
                    if (gameplay.isGameOver()) {
                        gameplay.startNewGame()
                        gameScore.startNewGame()
                    } else {
                        gameplay.advanceRound()
                        gameScore.advanceRound(gameplay.currentRound)
                    }
                    navigation.emit(GameResultsNavigation.StartNextRound)
                }
            }
            GameResultsEvent.GameOver -> {
                state = state.copy(isGameOver = true)
            }
        }
    }
}