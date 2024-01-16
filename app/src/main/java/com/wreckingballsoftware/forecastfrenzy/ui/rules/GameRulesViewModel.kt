package com.wreckingballsoftware.forecastfrenzy.ui.rules

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.wreckingballsoftware.forecastfrenzy.ui.rules.models.RulesEvents
import com.wreckingballsoftware.forecastfrenzy.ui.rules.models.RulesNavigation
import com.wreckingballsoftware.forecastfrenzy.ui.rules.models.RulesState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class GameRulesViewModel(
    handle: SavedStateHandle,
): ViewModel() {
    @OptIn(SavedStateHandleSaveableApi::class)
    var state by handle.saveable {
        mutableStateOf(RulesState())
    }
    val navigation = MutableSharedFlow<RulesNavigation>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_LATEST,
    )

    fun handleEvent(event: RulesEvents) {
        when (event) {
            RulesEvents.PlayGame -> {
                viewModelScope.launch(Dispatchers.Main) {
                    navigation.emit(RulesNavigation.StartGame)
                }
            }
        }
    }
}