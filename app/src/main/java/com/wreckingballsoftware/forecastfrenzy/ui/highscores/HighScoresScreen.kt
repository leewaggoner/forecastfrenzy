package com.wreckingballsoftware.forecastfrenzy.ui.highscores

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import com.wreckingballsoftware.forecastfrenzy.R
import com.wreckingballsoftware.forecastfrenzy.domain.models.HighScore
import com.wreckingballsoftware.forecastfrenzy.ui.compose.FrenzyErrorAlert
import com.wreckingballsoftware.forecastfrenzy.ui.highscores.models.HighScoresEvent
import com.wreckingballsoftware.forecastfrenzy.ui.highscores.models.HighScoresState
import com.wreckingballsoftware.forecastfrenzy.ui.navigation.NavGraph
import com.wreckingballsoftware.forecastfrenzy.ui.theme.dimensions
import com.wreckingballsoftware.forecastfrenzy.ui.theme.forecastTypography
import org.koin.androidx.compose.getViewModel

@Composable
fun HighScoresScreen(
    navGraph: NavGraph,
    viewModel: HighScoresViewModel = getViewModel(),
) {
    HighScoresScreenContent(
        state = viewModel.state,
        handleEvent = viewModel::handleEvent
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
fun HighScoresScreenContent(
    state: HighScoresState,
    handleEvent: (HighScoresEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = MaterialTheme.dimensions.spaceMedium),
    ) {
        Text(
            text = stringResource(id = R.string.high_scores_title),
            style = MaterialTheme.forecastTypography.headlineCentered,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState()),
        ) {
            state.highScores.forEach { highScore ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier.width(MaterialTheme.dimensions.highScoreBoxWidth)
                    ) {
                        Text(
                            text = highScore.name,
                            style = if (highScore.isPlayer) {
                                MaterialTheme.forecastTypography.highScoreHighlightText
                            } else {
                                MaterialTheme.forecastTypography.highScoreText
                            },
                        )
                    }
                    Spacer(modifier = Modifier.width(MaterialTheme.dimensions.spaceSmall))
                    Box(
                        modifier = Modifier.width(MaterialTheme.dimensions.highScoreBoxWidth)
                    ) {
                        Text(
                            text = highScore.score.toString(),
                            style = if (highScore.isPlayer) {
                                MaterialTheme.forecastTypography.highScoreHighlightText
                            } else {
                                MaterialTheme.forecastTypography.highScoreText
                            },
                        )
                    }
                }
            }
        }
    }

    if (state.errorMessage != null) {
        FrenzyErrorAlert(message = state.errorMessage) {
            handleEvent(HighScoresEvent.DismissErrorDialog)
        }
    }
}

@Preview
@Composable
fun HighScoresScreenPreview() {
    HighScoresScreenContent(
        state = HighScoresState(
            highScores = listOf(
                HighScore(0, "John", 100),
                HighScore(1, "Jane", 90, true),
                HighScore(2, "Jim", 80),
            )
        ),
        handleEvent = { },
    )
}
