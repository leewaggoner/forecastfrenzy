package com.wreckingballsoftware.forecastfrenzy.ui.results.content

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
fun ScoreContent(
    modifier: Modifier = Modifier,
    state: GameResultsState,
) {
    Column(
        modifier = modifier.then(Modifier.fillMaxSize()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
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
        if (state.isGameOver) {
            Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
            Text(
                text = stringResource(id = R.string.high_score),
                style = MaterialTheme.forecastTypography.bodyCentered,
            )
            Text(
                text = state.highScore.toString(),
                style = MaterialTheme.forecastTypography.headline,
            )
        }
    }
}

@Preview
@Composable
fun ScoreContentPreview() {
    ScoreContent(
        state = GameResultsState(
            bet = 100,
            roundScore = 100,
            timeBonus = 10,
            totalScore = 1110,
            isGameOver = true,
        )
    )
}