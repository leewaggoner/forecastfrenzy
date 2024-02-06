package com.wreckingballsoftware.forecastfrenzy.ui.gameplay.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wreckingballsoftware.forecastfrenzy.R
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models.GameplayState
import com.wreckingballsoftware.forecastfrenzy.ui.theme.forecastTypography

@Composable
fun GameplayTopRowContent(
    modifier: Modifier = Modifier,
    state: GameplayState,
) {
    Row(
        modifier = modifier.then(
            Modifier.fillMaxWidth(),
        ),
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
}

@Preview
@Composable
fun GameplayTopRowPreview() {
    GameplayTopRowContent(
        state = GameplayState(
            curRound = 1,
            yourPoints = 1000,
            roundPoints = 200
        )
    )
}