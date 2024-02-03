package com.wreckingballsoftware.forecastfrenzy.ui.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wreckingballsoftware.forecastfrenzy.R
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
        onNameChanged = viewModel::onNameChanged,
        onEmailChanged = viewModel::onEmailChanged,
        onLogin = viewModel::onLogin,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    state: LoginState,
    onNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onLogin: () -> Unit,
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
        OutlinedTextField(
            value = state.name,
            onValueChange = onNameChanged,
            label = { Text(text = stringResource(id = R.string.name)) },
            placeholder = { Text(text = stringResource(id = R.string.name_label)) }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceSmall))
        OutlinedTextField(
            value = state.email,
            onValueChange = onEmailChanged,
            label = { Text(text = stringResource(id = R.string.email)) },
            placeholder = { Text(text = stringResource(id = R.string.email_label)) }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.dimensions.spaceMedium))
        Button(
            modifier = Modifier
                .width(MaterialTheme.dimensions.buttonWidth),
            onClick = onLogin,
        ) {
            Text(text = stringResource(id = R.string.login))
        }
    }
}

@Preview
@Composable
fun LoginContentPreview() {
    LoginContent(
        state = LoginState(),
        onNameChanged = { },
        onEmailChanged = { },
        onLogin = { },
    )
}