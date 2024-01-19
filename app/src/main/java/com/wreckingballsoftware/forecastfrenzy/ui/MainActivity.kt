package com.wreckingballsoftware.forecastfrenzy.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.wreckingballsoftware.forecastfrenzy.ui.navigation.ForecastHost
import com.wreckingballsoftware.forecastfrenzy.ui.theme.ForecastFrenzyTheme
import com.wreckingballsoftware.forecastfrenzy.utils.NetworkConnection
import org.koin.android.ext.android.inject
import org.koin.core.parameter.ParametersHolder

class MainActivity : ComponentActivity() {
    private val networkConnection: NetworkConnection by inject(
        parameters = { ParametersHolder(mutableListOf(lifecycleScope)) }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            val connectionState by networkConnection.connection.collectAsStateWithLifecycle()
            ForecastFrenzyTheme(
                darkTheme = true
            ) {
                Surface {
                    ForecastHost(connectionState)
                }
            }
        }
    }
}
