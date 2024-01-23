package com.wreckingballsoftware.forecastfrenzy.ui.results

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
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
        if (gameplay.isGameOver()) {
            handleEvent(GameResultsEvent.GameOver)
        }
    }

    fun handleEvent(event: GameResultsEvent) {
        when (event) {
            GameResultsEvent.StartNextRound -> {
                viewModelScope.launch(Dispatchers.Main) {
                    if (gameplay.isGameOver()) {
                        gameplay.startNewGame()
                    } else {
                        gameplay.advanceRound()
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