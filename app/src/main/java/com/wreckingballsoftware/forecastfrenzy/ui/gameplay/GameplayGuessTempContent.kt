package com.wreckingballsoftware.forecastfrenzy.ui.gameplay

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wreckingballsoftware.forecastfrenzy.R
import com.wreckingballsoftware.forecastfrenzy.domain.MAXIMUM_TEMP
import com.wreckingballsoftware.forecastfrenzy.domain.MINIMUM_TEMP
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models.GameplayEvent
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models.GameplayState
import com.wreckingballsoftware.forecastfrenzy.ui.theme.dimensions
import com.wreckingballsoftware.forecastfrenzy.ui.theme.forecastTypography

@Composable
fun GameplayGuessTempContent(
    modifier: Modifier = Modifier,
    state: GameplayState,
    handleEvent: (GameplayEvent) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.question, state.city),
            style = MaterialTheme.forecastTypography.titleCentered,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
        Text(
            text = stringResource(id = R.string.degrees, state.curGuess),
            style = MaterialTheme.forecastTypography.title
        )
        Slider(
            value = state.curGuess.toFloat(),
            onValueChange = { handleEvent(GameplayEvent.GuessChanged(it)) },
            valueRange = MINIMUM_TEMP..MAXIMUM_TEMP,
            steps = (MAXIMUM_TEMP - MINIMUM_TEMP).toInt(),
        )
    }
}

@Preview
@Composable
fun GameplayGuessTempPreview() {
    GameplayGuessTempContent(
        state = GameplayState(),
        handleEvent = { }
    )
}