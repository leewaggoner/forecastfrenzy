package com.wreckingballsoftware.forecastfrenzy.ui.results

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wreckingballsoftware.forecastfrenzy.R
import com.wreckingballsoftware.forecastfrenzy.ui.compose.FrenzyButton
import com.wreckingballsoftware.forecastfrenzy.ui.compose.FrenzyErrorAlert
import com.wreckingballsoftware.forecastfrenzy.ui.navigation.NavGraph
import com.wreckingballsoftware.forecastfrenzy.ui.results.content.ScoreContent
import com.wreckingballsoftware.forecastfrenzy.ui.results.content.TempGuessContent
import com.wreckingballsoftware.forecastfrenzy.ui.results.models.GameResultsEvent
import com.wreckingballsoftware.forecastfrenzy.ui.results.models.GameResultsNavigation
import com.wreckingballsoftware.forecastfrenzy.ui.results.models.GameResultsState
import com.wreckingballsoftware.forecastfrenzy.ui.theme.dimensions
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.ParametersHolder

@Composable
fun GameResultsScreen(
    navGraph: NavGraph,
    cityName: String,
    guess: Int,
    bet: Int,
    seconds: Int,
    viewModel: GameResultsViewModel = getViewModel(
        parameters = { ParametersHolder(mutableListOf(cityName, guess, bet, seconds)) }
    )
) {
    val navigation = viewModel.navigation.collectAsStateWithLifecycle(null)
    navigation.value?.let { nav ->
        when (nav) {
            GameResultsNavigation.StartNextRound ->navGraph.navigateToGameplayScreen()
        }
    }

    GameResultsContent(
        state = viewModel.state,
        handleEvent = viewModel::handleEvent,
    )

    if (viewModel.state.isLoading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { },
                )
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun GameResultsContent(
    state: GameResultsState,
    handleEvent: (GameResultsEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MaterialTheme.dimensions.padding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .weight(1.0f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TempGuessContent(state = state)
            Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
            ScoreContent(state = state)
        }
        FrenzyButton(
            modifier = Modifier
                .padding(bottom = MaterialTheme.dimensions.spaceMedium),
            textId = state.buttonTextId
        ) { handleEvent(GameResultsEvent.StartNextRound) }
    }

    if (state.errorMessage != null) {
        FrenzyErrorAlert(message = state.errorMessage) {
            handleEvent(GameResultsEvent.DismissErrorDialog)
        }
    }
}

@Preview
@Composable
fun GameResultsContentPreview() {
    GameResultsContent(
        state = GameResultsState(
            headlineTextId = R.string.round_results,
            currentRound = 1,
            buttonTextId = R.string.next_round,
        ),
        handleEvent = { }
    )
}