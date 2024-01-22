package com.wreckingballsoftware.forecastfrenzy.domain

import android.os.CountDownTimer

const val MAX_TIME_MILLISECONDS = MAX_TIME * 1000L
const val TIMER_INTERVAL = 1000L

class GameTimer : CountDownTimer(
        MAX_TIME_MILLISECONDS,
        TIMER_INTERVAL,
) {
    private var onTick: (() -> Unit)? = null
    private var onFinish: (() -> Unit)? = null

    fun startTimer(onTick: () -> Unit, onFinish: () -> Unit) {
        this.onTick = onTick
        this.onFinish = onFinish
        this.start()
    }

    override fun onTick(millisUntilFinished: Long) {
        onTick?.let { tickFunc ->
            tickFunc()
        }
    }

    override fun onFinish() {
        onFinish?.let { finishFunc ->
            finishFunc()
        }
    }
}