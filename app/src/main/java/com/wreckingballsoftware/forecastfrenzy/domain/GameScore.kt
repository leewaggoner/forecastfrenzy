package com.wreckingballsoftware.forecastfrenzy.domain

import kotlin.math.abs
import kotlin.math.roundToInt

const val STARTING_PLAYER_POINTS = 1000

class GameScore {
    private val roundPoints = listOf(
        200,
        400,
        600,
        800,
        1000,
    )
    private val antePoints = listOf(
        100,
        200,
        300,
        400,
        500,
    )
    private var currentAntePoints = antePoints[0]
    var roundMaxPoints = roundPoints[0]
        private set
    var currentScore = STARTING_PLAYER_POINTS
        private set
    var roundScore = 0
        private set
    var timeBonus = 0
        private set

    fun advanceRound(currentRound: Int) {
        roundMaxPoints = roundPoints[currentRound]
        currentAntePoints = antePoints[currentRound]
        roundScore = 0
        timeBonus = 0
    }

    fun startNewGame() {
        currentScore = STARTING_PLAYER_POINTS
        roundMaxPoints = roundPoints[0]
        currentAntePoints = antePoints[0]
        roundScore = 0
        timeBonus = 0
    }

    fun getCurrentAntePoints(): List<String> =
        (currentAntePoints..roundMaxPoints step 10).map { it.toString() }

    fun handleGuess(guess: Int, actualTemp: Int, bet: Int, seconds: Int) {
        val absGuess = abs(actualTemp - guess)
        roundScore = when (absGuess) {
            0 -> {
                //perfect guess
                timeBonus = (MAX_TIME - seconds) * 5
                bet * 2
            }
            in 1..4 -> {
                //guess is within 5 of the actual temp
                timeBonus = (MAX_TIME - seconds) * 5
                val percent = ((5f - absGuess) * 2f) / 10f
                (bet * percent).roundToInt()
            }
            5 -> {
                //guess is 5 away from the actual temperature
                timeBonus = (MAX_TIME - seconds) * 5
                10
            }
            in 6..9 -> {
                //guess is more than 5, but within 10 of the actual temperature
                val percent = ((10f - absGuess) * 2f) / 10f
                val points = bet.toFloat() * percent
                -points.roundToInt()
            }
            else -> {
                //guess is 10 or more away from the actual temperature
                -bet
            }
        }
        currentScore += roundScore + timeBonus
    }
}