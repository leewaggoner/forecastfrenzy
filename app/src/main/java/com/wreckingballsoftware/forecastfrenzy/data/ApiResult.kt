package com.wreckingballsoftware.forecastfrenzy.data

sealed class ApiResult<T> {
    class Success<T>(val data: T) : ApiResult<T>()
    class Error<T>(val errorMessage: String) : ApiResult<T>()
}
