package com.fawry.movieapp.data.model

data class Result<out T>(
    val status: Status,
    val data: T?,
    val message: String,
) {

    companion object {

        fun <T> loading(message: String): Result<T> =
            Result(
                status = Status.LOADING,
                data = null,
                message = message,
            )

        fun <T> complete(data: T, message: String): Result<T> =
            Result(
                status = Status.COMPLETE,
                data = data,
                message = message,
            )

        fun <T> error(message: String): Result<T> =
            Result(
                status = Status.ERROR,
                data = null,
                message = message,
            )

    }
}