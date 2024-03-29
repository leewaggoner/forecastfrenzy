package com.wreckingballsoftware.forecastfrenzy.ui.rules

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wreckingballsoftware.forecastfrenzy.R
import com.wreckingballsoftware.forecastfrenzy.ui.compose.FrenzyButton
import com.wreckingballsoftware.forecastfrenzy.ui.navigation.NavGraph
import com.wreckingballsoftware.forecastfrenzy.ui.rules.models.RulesEvents
import com.wreckingballsoftware.forecastfrenzy.ui.rules.models.RulesNavigation
import com.wreckingballsoftware.forecastfrenzy.ui.rules.models.RulesState
import com.wreckingballsoftware.forecastfrenzy.ui.theme.dimensions
import com.wreckingballsoftware.forecastfrenzy.ui.theme.forecastTypography
import org.koin.androidx.compose.getViewModel

@Composable
fun GameRulesScreen(
    navGraph: NavGraph,
    viewModel: GameRulesViewModel = getViewModel(),
) {
    val navigation = viewModel.navigation.collectAsStateWithLifecycle(null)
    navigation.value?.let { nav ->
        when (nav) {
            RulesNavigation.StartGame -> navGraph.navigateToGameplayScreen()
        }
    }

    GameRulesScreenContent(
        viewModel.state,
        viewModel::handleEvent
    )
}

@Composable
fun GameRulesScreenContent(
    state: RulesState,
    handleEvent: (RulesEvents) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = MaterialTheme.dimensions.spaceMedium)
            .padding(horizontal = MaterialTheme.dimensions.padding)
            .padding(bottom = MaterialTheme.dimensions.padding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier
                .weight(1.0f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.forecastTypography.headlineCentered,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
            Text(
                text = stringResource(id = R.string.rules, state.numRounds),
                style = MaterialTheme.forecastTypography.bodyCentered,
            )
        }
        FrenzyButton(
            modifier = Modifier
                .padding(bottom = MaterialTheme.dimensions.spaceMedium),
            text = stringResource(id = R.string.start_game)
        ) { handleEvent(RulesEvents.PlayGame) }
    }
}

@Preview
@Composable
fun GameRulesScreenPreview() {
    GameRulesScreenContent(
        state = RulesState(),
        handleEvent = { }
    )
}