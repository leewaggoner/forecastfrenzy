package com.wreckingballsoftware.forecastfrenzy.ui.highscores

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.wreckingballsoftware.forecastfrenzy.data.repositories.HighScoreRepo

class HighScoresViewModel(
    handle: SavedStateHandle,
    highScoreRepo: HighScoreRepo,
) : ViewModel() {
}