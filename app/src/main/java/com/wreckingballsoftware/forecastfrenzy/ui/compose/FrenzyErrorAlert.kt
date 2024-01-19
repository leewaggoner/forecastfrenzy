package com.wreckingballsoftware.forecastfrenzy.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.wreckingballsoftware.forecastfrenzy.R

@Composable
fun FrenzyErrorAlert(
    message: String,
    onDismissAlert: () -> Unit,
) {
    FrenzyAlert(
        title = stringResource(id = R.string.error),
        message = message,
        onDismissRequest = onDismissAlert,
        onConfirmAlert = onDismissAlert,
        onDismissAlert = null,
    )
}
