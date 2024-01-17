package com.wreckingballsoftware.forecastfrenzy.di

import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.GameplayViewModel
import com.wreckingballsoftware.forecastfrenzy.ui.rules.GameRulesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        GameRulesViewModel(
            handle = get(),
        )
    }

    viewModel {
        GameplayViewModel(
            handle = get(),
        )
    }
}