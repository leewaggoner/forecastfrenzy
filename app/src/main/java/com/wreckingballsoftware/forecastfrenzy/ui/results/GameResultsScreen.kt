package com.wreckingballsoftware.forecastfrenzy.ui.results

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wreckingballsoftware.forecastfrenzy.R
import com.wreckingballsoftware.forecastfrenzy.ui.navigation.NavGraph
import com.wreckingballsoftware.forecastfrenzy.ui.results.models.GameResultsEvent
import com.wreckingballsoftware.forecastfrenzy.ui.results.models.GameResultsNavigation
import com.wreckingballsoftware.forecastfrenzy.ui.results.models.GameResultsState
import com.wreckingballsoftware.forecastfrenzy.ui.theme.dimensions
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
            Text(
                text = stringResource(id = state.headlineTextId, state.currentRound),
                style = MaterialTheme.forecastTypography.headlineCentered,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
            Text(
                text = stringResource(id = R.string.current_temperature, state.cityName),
                style = MaterialTheme.forecastTypography.bodyCentered,
            )
            Text(
                text = state.actualTemp.toString(),
                style = MaterialTheme.forecastTypography.titleCentered,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
            Text(
                text = stringResource(id = R.string.your_guess, state.guess),
                style = MaterialTheme.forecastTypography.titleCentered,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
            Text(
                text = stringResource(id = R.string.you_bet, state.bet),
                style = MaterialTheme.forecastTypography.bodyCentered,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
            Text(
                text = stringResource(id = R.string.score_for_round),
                style = MaterialTheme.forecastTypography.bodyCentered,
            )
            Text(
                text = "${state.roundScore} plus time bonus ${state.timeBonus}",
                style = MaterialTheme.forecastTypography.titleCentered,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
            Text(
                text = stringResource(id = R.string.total_score),
                style = MaterialTheme.forecastTypography.bodyCentered,
            )
            Text(
                text = state.totalScore.toString(),
                style = MaterialTheme.forecastTypography.headline,
            )
        }
        Button(
            modifier = Modifier
                .width(MaterialTheme.dimensions.buttonWidth)
                .padding(bottom = MaterialTheme.dimensions.spaceMedium),
            onClick = { handleEvent(GameResultsEvent.StartNextRound) }
        ) {
            Text(
                text = stringResource(id = state.buttonTextId)
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