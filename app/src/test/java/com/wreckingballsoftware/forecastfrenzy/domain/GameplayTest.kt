package com.wreckingballsoftware.forecastfrenzy.domain

import com.wreckingballsoftware.forecastfrenzy.data.models.CityResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.CityResult
import com.wreckingballsoftware.forecastfrenzy.data.models.Current
import com.wreckingballsoftware.forecastfrenzy.data.models.NetworkResponse
import com.wreckingballsoftware.forecastfrenzy.data.models.TemperatureResponse
import com.wreckingballsoftware.forecastfrenzy.data.repositories.CityRepo
import com.wreckingballsoftware.forecastfrenzy.data.repositories.WeatherRepo
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GameplayTest {
    private val cityRepo = mockk<CityRepo>()
    private val weatherRepo = mockk<WeatherRepo>()
    private val gameTimer = mockk<GameTimer>()
    private val cityResponseSuccess = CityResponse(
        results = listOf(
            CityResult(
                name = "New York",
                country = "USA",
                countryCode = "US",
                population = 10000001,
                latitude = "40.7128",
                longitude = "74.0060"
            )
        ),
        errorCode = "0",
        message = null
    )
    private val cityResponseError = CityResponse(
        results = listOf(),
        errorCode = "0",
        message = "Error getting city"
    )
    private val tempResponseSuccess = TemperatureResponse(
        current = Current("100"),
        cod = "0",
        message = null,
    )
    private val tempResponseError = TemperatureResponse(
        current = Current("0"),
        cod = "0",
        message = "Error getting temp"
    )

    @Before
    fun setUp() {
    }

    @Test
    fun testAdvanceRound() {
        val gameplay = Gameplay(
            cityRepo = cityRepo,
            weatherRepo = weatherRepo,
            gameTimer = gameTimer,
        )
        gameplay.advanceRound()
        assert(gameplay.currentRound == 1)
    }

    @Test
    fun testIsGameOver() {
        val gameplay = Gameplay(
            cityRepo = cityRepo,
            weatherRepo = weatherRepo,
            gameTimer = gameTimer,
        )
        assert(!gameplay.isGameOver())
        gameplay.advanceRound()
        gameplay.advanceRound()
        gameplay.advanceRound()
        gameplay.advanceRound()
        assert(gameplay.isGameOver())
    }

    @Test
    fun testStartNewGame() {
        val gameplay = Gameplay(
            cityRepo = cityRepo,
            weatherRepo = weatherRepo,
            gameTimer = gameTimer,
        )
        gameplay.startNewGame()
        assert(gameplay.currentRound == 0)
    }

    @Test
    fun testGetNewCity() {
        coEvery {
            cityRepo.getCity(
                filter = "population > 10000000",
                orderBy = any(),
            )
        } returns NetworkResponse.Success(cityResponseSuccess)

        val gameplay = Gameplay(
            cityRepo = cityRepo,
            weatherRepo = weatherRepo,
            gameTimer = gameTimer,
        )

        runBlocking {
            val cityName = gameplay.getNewCity { }
            assert(cityName == "New York, USA")
        }
    }

    @Test
    fun testGetNewCityApiError() {
        coEvery {
            cityRepo.getCity(
                filter = "population > 10000000",
                orderBy = any(),
            )
        } returns NetworkResponse.Success(cityResponseError)

        val gameplay = Gameplay(
            cityRepo = cityRepo,
            weatherRepo = weatherRepo,
            gameTimer = gameTimer,
        )

        runBlocking {
            gameplay.getNewCity {
                assert( it == "Error code 0: Error getting city")
            }
        }
    }
    @Test
    fun testGetNewCityUnknownNetworkError() {
        coEvery {
            cityRepo.getCity(
                filter = "population > 10000000",
                orderBy = any(),
            )
        } returns NetworkResponse.Error.UnknownNetworkError(Exception("Unknown network error"), 0)

        val gameplay = Gameplay(
            cityRepo = cityRepo,
            weatherRepo = weatherRepo,
            gameTimer = gameTimer,
        )

        runBlocking {
            gameplay.getNewCity {
                assert( it == "Error code 0: Unknown network error")
            }
        }
    }

    @Test
    fun testGetTemp() {
        coEvery {
            weatherRepo.getWeather(
                lat = any(),
                lon = any(),
            )
        } returns NetworkResponse.Success(tempResponseSuccess)

        val gameplay = Gameplay(
            cityRepo = cityRepo,
            weatherRepo = weatherRepo,
            gameTimer = gameTimer,
        )

        runBlocking {
            var temp = 0
            gameplay.getTemp(
                onSuccess = { temp = it },
                onError = { }
            )
            assert(temp == 100)
        }
    }

    @Test
    fun testGetTempApiError() {
        coEvery {
            weatherRepo.getWeather(
                lat = any(),
                lon = any(),
            )
        } returns NetworkResponse.Success(tempResponseError)

        val gameplay = Gameplay(
            cityRepo = cityRepo,
            weatherRepo = weatherRepo,
            gameTimer = gameTimer,
        )

        runBlocking {
            gameplay.getTemp(
                onSuccess = { },
                 onError = {
                    assert( it == "Error code 0: Error getting temp")
                 }
            )
        }
    }

    @Test
    fun getTempUnknownNetworkError() {
        coEvery {
            weatherRepo.getWeather(
                lat = any(),
                lon = any(),
            )
        } returns NetworkResponse.Error.UnknownNetworkError(Exception("Unknown network error"), 0)

        val gameplay = Gameplay(
            cityRepo = cityRepo,
            weatherRepo = weatherRepo,
            gameTimer = gameTimer,
        )

        runBlocking {
            gameplay.getTemp(
                onSuccess = { },
                onError ={
                assert( it == "Error code 0: Unknown network error")
            }
            )
        }
    }
}