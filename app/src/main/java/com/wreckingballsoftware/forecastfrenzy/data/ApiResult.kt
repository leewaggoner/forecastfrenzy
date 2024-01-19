package com.wreckingballsoftware.forecastfrenzy.data

sealed class ApiResult<T>(
    val data: T? = null,
    val message: String = "",
) {
    class Success<T>(data: T) : ApiResult<T>(data = data)
    class Error<T>(errorMessage: String) : ApiResult<T>(message = errorMessage)
    class Loading<T>() : ApiResult<T>()
}
