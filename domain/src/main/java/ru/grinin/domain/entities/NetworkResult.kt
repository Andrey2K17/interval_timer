package ru.grinin.domain.entities


sealed class NetworkResult<T> {
    class Success<T>(val data: T) : NetworkResult<T>() {
        override fun toString(): String {
            return "${javaClass.simpleName} : ${data.toString()}"
        }
    }

    class Loading<T>() : NetworkResult<T>() {
        override fun toString(): String {
            return javaClass.simpleName
        }
    }

    class Error<T>(val errorBody: ErrorBody) : NetworkResult<T>() {
        override fun toString(): String {
            return "${javaClass.simpleName} : $errorBody"
        }
    }

    inline fun <R> transform(block: (T) -> R): NetworkResult<R> {
        return when (this) {
            is Error -> Error(errorBody)
            is Loading -> Loading()
            is Success -> Success(block(data))
        }
    }

    inline fun doOnError(block: (errorBody: ErrorBody) -> Unit): NetworkResult<T> {
        if (this is Error) {
            block(errorBody)
        }
        return this
    }

    inline fun doOnLoading(block: () -> Unit): NetworkResult<T> {
        if (this is Loading) {
            block()
        }
        return this
    }

    inline fun doOnSuccess(block: (T) -> Unit): NetworkResult<T> {
        if (this is Success) {
            block(data)
        }
        return this
    }

    fun getDataOrNull() = if (this is Success) data else null
}


class ErrorBody(val error: ApiUnifiedError, val throwable: Throwable? = null) {
    val fullErrorMessage get() = "Error\nmessage : ${error.message}\n ${throwable?.stackTraceToString().orEmpty()}"

    override fun toString(): String {
        return fullErrorMessage
    }
}