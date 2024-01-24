package com.wreckingballsoftware.forecastfrenzy.ui.compose

import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.wreckingballsoftware.forecastfrenzy.ui.theme.dimensions

@Composable
fun FrenzyButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    textId: Int = 0,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.then(
            Modifier
                .width(MaterialTheme.dimensions.buttonWidth)
        ),
        onClick = onClick
    ) {
        Text(
            text = text ?: stringResource(id = textId)
        )
    }
}

@Preview
@Composable
fun FrenzyButtonPreview() {
    FrenzyButton(
        text = "Push Me",
    ) {

    }
}