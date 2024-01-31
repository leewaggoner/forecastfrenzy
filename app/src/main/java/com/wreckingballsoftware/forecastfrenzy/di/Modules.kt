package com.wreckingballsoftware.forecastfrenzy.di

import android.content.Context
import android.net.ConnectivityManager
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.wreckingballsoftware.forecastfrenzy.BuildConfig
import com.wreckingballsoftware.forecastfrenzy.data.repositories.CityRepo
import com.wreckingballsoftware.forecastfrenzy.data.repositories.HighScoreRepo
import com.wreckingballsoftware.forecastfrenzy.data.repositories.WeatherRepo
import com.wreckingballsoftware.forecastfrenzy.data.storage.CityService
import com.wreckingballsoftware.forecastfrenzy.data.storage.DataStoreWrapper
import com.wreckingballsoftware.forecastfrenzy.data.storage.HighScoreService
import com.wreckingballsoftware.forecastfrenzy.data.storage.WeatherService
import com.wreckingballsoftware.forecastfrenzy.domain.GameScore
import com.wreckingballsoftware.forecastfrenzy.domain.GameTimer
import com.wreckingballsoftware.forecastfrenzy.domain.Gameplay
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.GameplayViewModel
import com.wreckingballsoftware.forecastfrenzy.ui.results.GameResultsViewModel
import com.wreckingballsoftware.forecastfrenzy.ui.rules.GameRulesViewModel
import com.wreckingballsoftware.forecastfrenzy.utils.NetworkConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val CONNECT_TIMEOUT = 30L
private const val READ_TIMEOUT = 30L
private const val WRITE_TIMEOUT = 30L
private const val DATA_STORE_NAME = "com.wreckingballsoftware.forecastfrenzy"

val appModule = module {
    viewModel {
        GameRulesViewModel(
            handle = get(),
        )
    }

    viewModel {
        GameplayViewModel(
            handle = get(),
            gameplay = get(),
            gameScore = get(),
        )
    }

    viewModel {params ->
        GameResultsViewModel(
            handle = get(),
            gameplay = get(),
            gameScore = get(),
            cityName = params[0],
            guess = params[1],
            bet = params[2],
            seconds = params[3],
        )
    }

    factory {
        CityRepo(
            cityService = get()
        )
    }

    factory<CityService> {
        createService(
            retrofit = retrofitService(
                url = BuildConfig.CITY_URL,
                okHttpClient = okHttp(),
                converterFactory = GsonConverterFactory.create(),
            )
        )
    }

    factory {
        WeatherRepo(
            weatherService = get()
        )
    }

    factory<WeatherService> {
        createService(
            retrofit = retrofitService(
                url = BuildConfig.WEATHER_URL,
                okHttpClient = okHttp(),
                converterFactory = GsonConverterFactory.create(),
            )
        )
    }

    single {
        Gameplay(
            cityRepo = get(),
            weatherRepo = get(),
            gameTimer = get(),
        )
    }

    factory {
        HighScoreRepo(
            highScoreService = get()
        )
    }

    factory<HighScoreService> {
        createService(
            retrofit = retrofitService(
                url = BuildConfig.HIGHSCORE_URL,
                okHttpClient = okHttp(),
                converterFactory = GsonConverterFactory.create(),
            )
        )
    }

    factory {
        DataStoreWrapper(getDataStore(androidContext()))
    }

    single {
        GameTimer()
    }

    single {
        GameScore(
            highScoreRepo = get()
        )
    }

    single { params ->
        NetworkConnection(androidContext().getSystemService(ConnectivityManager::class.java), params[0])
    }
}

inline fun <reified T> createService(retrofit: Retrofit) : T = retrofit.create(T::class.java)

private fun retrofitService(
    url: String,
    okHttpClient: OkHttpClient,
    converterFactory: GsonConverterFactory
) = Retrofit.Builder().apply {
    baseUrl(url)
    client(okHttpClient)
    addConverterFactory(converterFactory)
}.build()

private fun okHttp(authInterceptor: Interceptor? = null) = OkHttpClient.Builder().apply {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    addInterceptor(interceptor)
    connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
    readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
    connectTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
    retryOnConnectionFailure(true)
    authInterceptor?.let {
        addInterceptor(authInterceptor)
    }
}.build()

private fun getDataStore(context: Context) : DataStore<Preferences> =
    PreferenceDataStoreFactory.create(
        corruptionHandler = ReplaceFileCorruptionHandler(produceNewData = { emptyPreferences() }),
        produceFile = { context.preferencesDataStoreFile(DATA_STORE_NAME) },
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    )
