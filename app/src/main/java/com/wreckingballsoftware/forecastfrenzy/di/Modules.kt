package com.wreckingballsoftware.forecastfrenzy.di

import android.net.ConnectivityManager
import com.wreckingballsoftware.forecastfrenzy.BuildConfig
import com.wreckingballsoftware.forecastfrenzy.data.CityRepo
import com.wreckingballsoftware.forecastfrenzy.data.CityService
import com.wreckingballsoftware.forecastfrenzy.data.WeatherRepo
import com.wreckingballsoftware.forecastfrenzy.data.WeatherService
import com.wreckingballsoftware.forecastfrenzy.domain.GameTimer
import com.wreckingballsoftware.forecastfrenzy.ui.gameplay.GameplayViewModel
import com.wreckingballsoftware.forecastfrenzy.ui.rules.GameRulesViewModel
import com.wreckingballsoftware.forecastfrenzy.utils.NetworkConnection
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

val appModule = module {
    viewModel {
        GameRulesViewModel(
            handle = get(),
        )
    }

    viewModel {
        GameplayViewModel(
            handle = get(),
            gameTimer = get(),
            weatherRepo = get(),
            cityRepo = get(),
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
        GameTimer()
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
