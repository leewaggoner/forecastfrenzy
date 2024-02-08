package com.wreckingballsoftware.forecastfrenzy.utils

import android.util.Patterns
import com.wreckingballsoftware.forecastfrenzy.ui.login.MAX_NAME_LENGTH

fun String.isValidNameLength(): Boolean {
    return this.isNotEmpty() && this.length <= MAX_NAME_LENGTH
}

fun String.isValidNameCharacters(): Boolean {
    return this.all { it.isLetterOrDigit() }
}

fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}