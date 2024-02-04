package com.wreckingballsoftware.forecastfrenzy.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wreckingballsoftware.forecastfrenzy.R
import com.wreckingballsoftware.forecastfrenzy.ui.compose.FrenzyErrorAlert
import com.wreckingballsoftware.forecastfrenzy.ui.login.models.LoginEvent
import com.wreckingballsoftware.forecastfrenzy.ui.login.models.LoginNavigation
import com.wreckingballsoftware.forecastfrenzy.ui.login.models.LoginState
import com.wreckingballsoftware.forecastfrenzy.ui.navigation.NavGraph
import com.wreckingballsoftware.forecastfrenzy.ui.theme.dimensions
import com.wreckingballsoftware.forecastfrenzy.ui.theme.forecastTypography
import org.koin.androidx.compose.getViewModel

@Composable
fun LoginScreen(
    navGraph: NavGraph,
    viewModel: LoginViewModel = getViewModel(),
) {
    val navigation = viewModel.navigation.collectAsStateWithLifecycle(null)
    navigation.value?.let { nav ->
        when (nav) {
            LoginNavigation.GoToRulesScreen ->navGraph.navigateToGameRulesScreen()
        }
    }

    LoginContent(
        state = viewModel.state,
        handleEvent = viewModel::handleEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun LoginContent(
    state: LoginState,
    handleEvent: (LoginEvent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(id = R.string.game_name),
            style = MaterialTheme.forecastTypography.headlineLargeCentered
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceLarge))
        Text(
            text = stringResource(id = R.string.please_log_in),
            style = MaterialTheme.forecastTypography.titleCentered,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceSmall))
        val nameError = state.invalidNameId != 0
        OutlinedTextField(
            value = state.name,
            onValueChange = { handleEvent(LoginEvent.UpdateName(it)) },
            label = { Text(text = stringResource(id = R.string.name)) },
            placeholder = { Text(text = stringResource(id = R.string.name_placeholder)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
            isError = nameError,
            supportingText = {
                if (nameError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = state.invalidNameId),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceSmall))
        val keyboard = LocalSoftwareKeyboardController.current
        val emailError = state.invalidEmailId != 0
        OutlinedTextField(
            value = state.email,
            onValueChange = { handleEvent(LoginEvent.UpdateEmail(it)) },
            label = { Text(text = stringResource(id = R.string.email)) },
            placeholder = { Text(text = stringResource(id = R.string.email_placeholder)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Go,
            ),
            keyboardActions = KeyboardActions(
                onGo = {
                    keyboard?.hide()
                    handleEvent(LoginEvent.LoginButtonPressed)
                }
            ),
            isError = emailError,
            supportingText = {
                if (emailError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = state.invalidEmailId),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
        Button(
            modifier = Modifier
                .width(MaterialTheme.dimensions.buttonWidth),
            onClick = {
                keyboard?.hide()
                handleEvent(LoginEvent.LoginButtonPressed)
            },
            enabled = state.loginEnabled,
        ) {
            Text(text = stringResource(id = R.string.login))
        }

        if (state.apiErrorMessage != null) {
            FrenzyErrorAlert(
                message = state.apiErrorMessage,
                onDismissAlert = { handleEvent(LoginEvent.DismissAlert) }
            )
        }
    }

    if (state.isLoading) {
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

@Preview
@Composable
fun LoginContentPreview() {
    LoginContent(
        state = LoginState(),
        handleEvent = { },
    )
}