package com.wreckingballsoftware.forecastfrenzy.ui.results

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.wreckingballsoftware.forecastfrenzy.R
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
        initResultsScreen()
    }

    private fun initResultsScreen() {
        handleEvent(GameResultsEvent.InitResultsScreen)
        viewModelScope.launch(Dispatchers.Main) {
            handleEvent(GameResultsEvent.Loading(isLoading = true))
            val gotCurrentTemp = getCurrentTemp()
            if (gameplay.isGameOver() && gotCurrentTemp) {
                handleHighScore()
            }
            handleEvent(GameResultsEvent.Loading(isLoading = false))
        }
    }

    private suspend fun getCurrentTemp(): Boolean {
        var success = false
        gameplay.getTemp(
            onSuccess = { temp ->
                gameScore.handleGuess(
                    guess = guess,
                    actualTemp = temp,
                    bet = bet,
                    seconds = seconds,
                )
                handleEvent(GameResultsEvent.HandleGuessResult(temp))
                success = true
            },
            onError =  { errorMessage ->
                handleEvent(GameResultsEvent.ApiError(errorMessage))
            }
        )
        return success
    }

    private suspend fun handleHighScore() {
        var success = updateHighScore()
        if (success) {
            success = getCurrentHighScore()
        }
        if (!success) {
            handleEvent(GameResultsEvent.UpdateHighScore(gameScore.getCachedHighScore()))
        }
    }

    private suspend fun updateHighScore(): Boolean {
        var updatedHighScore = false
        gameScore.updateHighScore(
            onSuccess = { success ->
                updatedHighScore = success
            },
            onError = { message ->
                handleEvent(GameResultsEvent.ApiError(message))
            }
        )
        return updatedHighScore
    }

    private suspend fun getCurrentHighScore(): Boolean {
        var gotHighScore = false
        gameScore.getHighScore(
            onSuccess = { highScore ->
                gotHighScore = true
                handleEvent(GameResultsEvent.UpdateHighScore(highScore))
            },
            onError = { message ->
                handleEvent(GameResultsEvent.ApiError(message))
            }
        )
        return gotHighScore
    }

    fun handleEvent(event: GameResultsEvent) {
        when (event) {
            is GameResultsEvent.Loading -> {
                state = state.copy(isLoading = event.isLoading)
            }
            is GameResultsEvent.InitResultsScreen -> {
                val isGameOver = gameplay.isGameOver()
                state = state.copy(
                    isGameOver = isGameOver,
                    buttonTextId = if (isGameOver) R.string.new_game else R.string.next_round,
                    headlineTextId = if (isGameOver) R.string.game_results else R.string.round_results,
                    currentRound = gameplay.currentRound + 1,
                    cityName = cityName,
                    guess = guess,
                    bet = bet,
                )
            }
            is GameResultsEvent.HandleGuessResult -> {
                state = state.copy(
                    actualTemp = event.actualTemperature,
                    totalScore = gameScore.currentScore,
                    roundScore = gameScore.roundScore,
                    timeBonus = gameScore.timeBonus,
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
            is GameResultsEvent.UpdateHighScore -> {
                state = state.copy(highScore = event.highScore)
            }
            GameResultsEvent.ViewHighScores -> {
                viewModelScope.launch(Dispatchers.Main) {
                    navigation.emit(GameResultsNavigation.ViewHighScores)
                }
            }
        }
    }
}