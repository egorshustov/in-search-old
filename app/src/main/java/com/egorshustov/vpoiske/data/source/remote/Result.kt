package com.egorshustov.vpoiske.data.source.remote

import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    fun getString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> when (exception.cause) {
                is ConnectException, is TimeoutException, is UnknownHostException -> {
                    "Ошибка соединения: отсутствует подключение к сети или сервер недоступен"
                }
                else -> {
                    "Error[exception=$exception]"
                }
            }
            is Loading -> "Loading"
        }
    }
}

val Result<*>.succeeded
    get() = this is Result.Success && data != null
