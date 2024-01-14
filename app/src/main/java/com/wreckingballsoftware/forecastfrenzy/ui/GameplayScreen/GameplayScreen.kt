package com.wreckingballsoftware.forecastfrenzy.ui.GameplayScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wreckingballsoftware.forecastfrenzy.ui.navigation.NavGraph
import com.wreckingballsoftware.forecastfrenzy.ui.theme.forecastTypography

@Composable
fun GameplayScreen(navGraph: NavGraph) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Gameplay Screen",
            style = MaterialTheme.forecastTypography.headline
        )
        Spacer(modifier = Modifier.height(64.dp))
        Button(
            onClick = { navGraph.navigateToGameResultsScreen() }
        ) {
            Text(text = "I Win!")
        }
    }
}
