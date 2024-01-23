package com.wreckingballsoftware.forecastfrenzy.ui.results

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wreckingballsoftware.forecastfrenzy.ui.navigation.NavGraph
import com.wreckingballsoftware.forecastfrenzy.ui.results.models.GameResultsEvent
import com.wreckingballsoftware.forecastfrenzy.ui.results.models.GameResultsNavigation
import com.wreckingballsoftware.forecastfrenzy.ui.results.models.GameResultsState
import com.wreckingballsoftware.forecastfrenzy.ui.theme.forecastTypography
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.ParametersHolder

@Composable
fun GameResultsScreen(
    navGraph: NavGraph,
    guess: Int,
    bet: Int,
    seconds: Int,
    viewModel: GameResultsViewModel = getViewModel(
        parameters = { ParametersHolder(mutableListOf(guess, bet, seconds)) }
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
}

@Composable
fun GameResultsContent(
    state: GameResultsState,
    handleEvent: (GameResultsEvent) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Game Results Screen",
            style = MaterialTheme.forecastTypography.headlineCentered
        )
        Button(
            onClick = { handleEvent(GameResultsEvent.StartNextRound) }
        ) {
            Text(
                text = if (state.isGameOver) "Start New Game" else "Start Next Round"
            )
        }
    }
}

@Preview
@Composable
fun GameResultsContentPreview() {
    GameResultsContent(
        state = GameResultsState(),
        handleEvent = { }
    )
}