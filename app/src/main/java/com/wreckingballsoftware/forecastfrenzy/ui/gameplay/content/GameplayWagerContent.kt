package com.wreckingballsoftware.forecastfrenzy.ui.gameplay.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wreckingballsoftware.forecastfrenzy.R
import com.wreckingballsoftware.forecastfrenzy.ui.compose.Picker
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models.GameplayEvent
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.models.GameplayState
import com.wreckingballsoftware.forecastfrenzy.ui.theme.dimensions
import com.wreckingballsoftware.forecastfrenzy.ui.theme.forecastTypography

@Composable
fun GameplayWagerContent(
    modifier: Modifier = Modifier,
    state: GameplayState,
    handleEvent: (GameplayEvent) -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.your_wager),
            style = MaterialTheme.forecastTypography.title,
        )
        Picker(
            modifier = Modifier
                .padding(top = MaterialTheme.dimensions.spaceSmall)
                .fillMaxWidth(0.5f),
            items = state.curAnteRange,
            visibleItemsCount = 5,
            textStyle = MaterialTheme.forecastTypography.body,
            onValueChanged = { newValue ->
                handleEvent(GameplayEvent.AnteChanged(newValue))
            },
        )
    }
}

@Preview
@Composable
fun GameplayWagerPreview() {
    GameplayWagerContent(
        state = GameplayState(curAnteRange = listOf("1", "2", "3", "4", "5")),
        handleEvent = { }
    )
}