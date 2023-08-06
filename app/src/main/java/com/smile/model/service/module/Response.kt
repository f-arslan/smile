package com.smile.model.service.module

sealed class Response<out T> {
    object Loading : Response<Nothing>()
    data class Success<out R>(val data: R) : Response<R>()
    data class Failure(val e: Exception) : Response<Nothing>()

}

fun <T, R> Response<T>.map(transform: (T) -> R): Response<R> {
    return when (this) {
        is Response.Success -> Response.Success(transform(data))
        is Response.Failure -> Response.Failure(e)
        is Response.Loading -> Response.Loading
    }
}

fun <T> Response<T>.getOrElse(default: T): T {
    return when(this) {
        is Response.Success -> data
        else -> default
    }
}