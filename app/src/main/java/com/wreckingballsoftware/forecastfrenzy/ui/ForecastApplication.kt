package com.wreckingballsoftware.forecastfrenzy.ui

import android.app.Application
import com.wreckingballsoftware.forecastfrenzy.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class ForecastApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(level = Level.INFO)
            androidContext(androidContext = this@ForecastApplication)
            modules(modules = appModule)
        }
    }
}