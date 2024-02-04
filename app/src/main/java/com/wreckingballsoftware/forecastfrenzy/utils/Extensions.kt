package com.wreckingballsoftware.forecastfrenzy.utils

import android.util.Patterns

fun String.isValidName(): Boolean {
    return this.all { it.isLetterOrDigit() }
}

fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}