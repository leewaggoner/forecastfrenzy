package com.wreckingballsoftware.forecastfrenzy.ui.gameplay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wreckingballsoftware.forecastfrenzy.R
import com.wreckingballsoftware.forecastfrenzy.ui.compose.FrenzyErrorAlert
import com.wreckingballsoftware.forecastfrenzy.ui.compose.Picker
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models.GameplayEvent
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models.GameplayNavigation
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models.GameplayState
import com.wreckingballsoftware.forecastfrenzy.ui.navigation.NavGraph
import com.wreckingballsoftware.forecastfrenzy.ui.theme.dimensions
import com.wreckingballsoftware.forecastfrenzy.ui.theme.forecastTypography
import org.koin.androidx.compose.getViewModel

@Composable
fun GameplayScreen(
    navGraph: NavGraph,
    viewModel: GameplayViewModel = getViewModel(),
) {
    val navigation = viewModel.navigation.collectAsStateWithLifecycle(null)
    navigation.value?.let { nav ->
        when (nav) {
            GameplayNavigation.ViewResults ->navGraph.navigateToGameResultsScreen()
        }
    }

    GameplayScreenContent(
        state = viewModel.state,
        handleEvent = viewModel::handleEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameplayScreenContent(
    state: GameplayState,
    handleEvent: (GameplayEvent) -> Unit,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(id = R.string.current_round),
                        style = MaterialTheme.forecastTypography.body,
                    )
                    Text(
                        text = (state.curRound + 1).toString(),
                        style = MaterialTheme.forecastTypography.body,
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(id = R.string.your_points),
                        style = MaterialTheme.forecastTypography.body,
                    )
                    Text(
                        text = state.yourPoints.toString(),
                        style = MaterialTheme.forecastTypography.body,
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = stringResource(id = R.string.round_points),
                        style = MaterialTheme.forecastTypography.body,
                    )
                    Text(
                        text = state.roundPoints.toString(),
                        style = MaterialTheme.forecastTypography.body,
                    )
                }
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(id = R.string.your_wager),
                    style = MaterialTheme.forecastTypography.title,
                )
                val values = remember { (CURRENT_ANTE..ROUND_POINTS step 10).map { it.toString() } }
                Picker(
                    modifier = Modifier
                        .padding(top = MaterialTheme.dimensions.spaceSmall)
                        .fillMaxWidth(0.5f),
                    items = values,
                    visibleItemsCount = 5,
                    textStyle = MaterialTheme.forecastTypography.body,
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(id = R.string.question, state.city),
                    style = MaterialTheme.forecastTypography.titleCentered,
                )
                Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
                Text(
                    text = stringResource(id = R.string.degrees, state.curGuess.toInt()),
                    style = MaterialTheme.forecastTypography.title
                )
                Slider(
                    value = state.curGuess,
                    onValueChange = { handleEvent(GameplayEvent.GuessChanged(it)) },
                    valueRange = -130f..140f,
                    steps = 270,
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
            Text(
                text = state.secondsRemaining.toString(),
                style = MaterialTheme.forecastTypography.timerText
            )
        }
        Button(
            modifier = Modifier
                .width(MaterialTheme.dimensions.buttonWidth)
                .padding(bottom = MaterialTheme.dimensions.spaceMedium),
            onClick = { handleEvent(GameplayEvent.DisplayResults) }
        ) {
            Text(
                text = stringResource(id = R.string.submit)
            )
        }
    }

    if (state.errorMessage != null) {
        FrenzyErrorAlert(message = state.errorMessage) {
            handleEvent(GameplayEvent.DismissErrorDialog)
        }
    }
}

@Preview
@Composable
fun GameplayScreenContentPreview() {
    GameplayScreenContent(
        state = GameplayState(),
        handleEvent = { },
    )
}