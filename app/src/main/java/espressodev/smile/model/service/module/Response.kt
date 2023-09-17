package espressodev.smile.model.service.module

sealed class Response<out T> {
    object Loading : Response<Nothing>()
    data class Success<out R>(val data: R) : Response<R>()
    data class Failure(val e: Exception) : Response<Nothing>()

}

sealed class GoogleResponse<out T> {
    object Loading : GoogleResponse<Nothing>()

    data class Success<out T>(
        val data: T?
    ) : GoogleResponse<T>()

    data class Failure(
        val e: Exception
    ) : GoogleResponse<Nothing>()
}

sealed interface LoadingState {
    object Idle : LoadingState
    object Loading : LoadingState
}