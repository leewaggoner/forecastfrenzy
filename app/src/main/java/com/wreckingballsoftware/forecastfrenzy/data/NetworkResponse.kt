package com.wreckingballsoftware.forecastfrenzy.data

import retrofit2.HttpException

sealed class NetworkResponse<out T> {
    data class Success<T>(val data: T) : NetworkResponse<T>()

    sealed class Error(open val exception: Exception, open val code: Int) : NetworkResponse<Nothing>() {
        data class TooManyRequests(
            override val exception: Exception,
            override val code: Int,
        ) : Error(exception, code)

        data class NotFound(
            override val exception: Exception,
            override val code: Int,
        ) : Error(exception, code)

        data class Unauthorized(
            override val exception: Exception,
            override val code: Int,
        ) : Error(exception, code)

        data class BadRequest(
            override val exception: Exception,
            override val code: Int,
        ) : Error(exception, code)

        data class ApiError(
            override val exception: Exception,
            override val code: Int,
        ) : Error(exception, code)

        data class ServerError(
            override val exception: Exception,
            override val code: Int,
        ) : Error(exception, code)

        data class UnknownNetworkError(
            override val exception: Exception,
            override val code: Int = 0,
        ) : Error(exception, code)
    }
}

fun HttpException.toNetworkErrorResponse(): NetworkResponse<Nothing> =
    when (val code = code()) {
        400 -> NetworkResponse.Error.BadRequest(this, code)
        401,
        403 -> NetworkResponse.Error.Unauthorized(this, code)
        404 -> NetworkResponse.Error.NotFound(this, code)
        429 -> NetworkResponse.Error.TooManyRequests(this, code)
        in 400..499 -> NetworkResponse.Error.ApiError(this, code)
        in 500..599 -> NetworkResponse.Error.ServerError(this, code)
        else -> NetworkResponse.Error.UnknownNetworkError(this, code)
    }
