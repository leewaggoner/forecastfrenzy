package com.wreckingballsoftware.forecastfrenzy.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.wreckingballsoftware.forecastfrenzy.ui.navigation.ForecastHost
import com.wreckingballsoftware.forecastfrenzy.ui.theme.ForecastFrenzyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContent {
            ForecastFrenzyTheme(
                darkTheme = true
            ) {
                Surface {
                    ForecastHost()
                }
            }
        }
    }
}
