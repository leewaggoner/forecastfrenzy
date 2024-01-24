package com.wreckingballsoftware.forecastfrenzy.ui.results

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wreckingballsoftware.forecastfrenzy.R
import com.wreckingballsoftware.forecastfrenzy.ui.results.models.GameResultsState
import com.wreckingballsoftware.forecastfrenzy.ui.theme.dimensions
import com.wreckingballsoftware.forecastfrenzy.ui.theme.forecastTypography

@Composable
fun TempGuessContent(
    modifier: Modifier = Modifier,
    state: GameResultsState,
) {
    Column(
        modifier = modifier.then(Modifier.fillMaxSize()),
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
    }
}

@Preview
@Composable
fun TempGuessContentPreview() {
    TempGuessContent(
        state = GameResultsState(
            headlineTextId = R.string.game_results,
            currentRound = 5,
            guess = 32,
        )
    )
}