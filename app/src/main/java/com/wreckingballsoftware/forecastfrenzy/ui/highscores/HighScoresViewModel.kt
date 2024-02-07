package com.wreckingballsoftware.forecastfrenzy.ui.highscores

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.wreckingballsoftware.forecastfrenzy.domain.GameScore
import com.wreckingballsoftware.forecastfrenzy.ui.highscores.models.HighScoresEvent
import com.wreckingballsoftware.forecastfrenzy.ui.highscores.models.HighScoresState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HighScoresViewModel(
    handle: SavedStateHandle,
    private val gameScore: GameScore,
) : ViewModel() {
    @OptIn(SavedStateHandleSaveableApi::class)
    var state by handle.saveable {
        mutableStateOf(HighScoresState())
    }

    init {
        getHighScores()
    }

    private fun getHighScores() {
        handleEvent(HighScoresEvent.Loading(true))
        viewModelScope.launch(Dispatchers.Main) {
            gameScore.getHighScores(
                onSuccess = { highScores ->
                    handleEvent(HighScoresEvent.DisplayHighScores(highScores))
                },
                onError = { errorMessage ->
                    handleEvent(HighScoresEvent.ApiError(errorMessage))
                }
            )
        }
    }

    fun handleEvent(event: HighScoresEvent) {
        state = when (event) {
            is HighScoresEvent.Loading -> {
                state.copy(isLoading = event.isLoading)
            }

            is HighScoresEvent.DisplayHighScores -> {
                state.copy(isLoading = false, highScores = event.highScores)
            }

            is HighScoresEvent.ApiError -> {
                state.copy(isLoading = false, errorMessage = event.errorMessage)
            }

            is HighScoresEvent.DismissErrorDialog -> {
                state.copy(errorMessage = null)
            }
        }
    }
}