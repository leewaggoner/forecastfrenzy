package com.wreckingballsoftware.forecastfrenzy.ui.gameplay

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wreckingballsoftware.forecastfrenzy.R
import com.wreckingballsoftware.forecastfrenzy.ui.compose.FrenzyButton
import com.wreckingballsoftware.forecastfrenzy.ui.compose.FrenzyErrorAlert
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.content.GameplayGuessTempContent
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.content.GameplayTopRowContent
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.content.GameplayWagerContent
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
            is GameplayNavigation.ViewResults ->navGraph.navigateToGameResultsScreen(
                nav.cityName,
                nav.guess,
                nav.bet,
                nav.seconds
            )
        }
    }

    GameplayScreenContent(
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
            GameplayTopRowContent(state = state)
            Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
            GameplayWagerContent(state = state, handleEvent = handleEvent)
            Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
            GameplayGuessTempContent(state = state, handleEvent = handleEvent)
            Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
            Text(
                text = state.secondsRemaining.toString(),
                style = MaterialTheme.forecastTypography.timerText
            )
        }
        FrenzyButton(
            modifier = Modifier
                .padding(bottom = MaterialTheme.dimensions.spaceMedium),
            text = stringResource(id = R.string.submit)
        ) { handleEvent(GameplayEvent.DisplayResults) }
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
        state = GameplayState(curAnteRange = listOf("1", "2", "3", "4", "5")),
        handleEvent = { },
    )
}