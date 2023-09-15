package com.smile.model.service.module

sealed class Response<out T> {
    object Loading : Response<Nothing>()
    data class Success<out R>(val data: R) : Response<R>()
    data class Failure(val e: Exception) : Response<Nothing>()

}

sealed class GoogleResponse<out T> {
    object Loading: GoogleResponse<Nothing>()

    data class Success<out T>(
        val data: T?
    ): GoogleResponse<T>()

    data class Failure(
        val e: Exception
    ): GoogleResponse<Nothing>()
}

fun <T, R> Response<T>.map(transform: (T) -> R): Response<R> {
    return when (this) {
        is Response.Success -> Response.Success(transform(data))
        is Response.Failure -> Response.Failure(e)
        is Response.Loading -> Response.Loading
    }
}