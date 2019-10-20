package com.egorshustov.vpoiske.data.source.remote

import java.net.ConnectException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

sealed class ResponseMessage {
    class Success(val message: String?) : ResponseMessage()
    class Error(val message: String?, val throwable: Throwable?) : ResponseMessage()

    fun getErrorMessage(): String? {
        return when (this) {
            is Success -> {
                null
            }
            is Error -> {
                when (throwable) {
                    is ConnectException, is TimeoutException, is UnknownHostException -> {
                        "Ошибка соединения: отсутствует подключение к сети или сервер недоступен"
                    }
                    else -> {
                        message ?: throwable.toString()
                    }
                }
            }
        }
    }
}