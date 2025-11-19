package ru.grinin.intervaltimer.entities

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update

sealed class UIState<out T> {

    data object None : UIState<Nothing>()
    data object Loading : UIState<Nothing>()
    class Error<T>(val errorBody: ErrorBodyUI) : UIState<T>()
    class Success<T>(val data: T) : UIState<T>()

    inline fun <R> transform(block: (T) -> R): UIState<R> {
        return when (this) {
            is Error -> Error(errorBody)
            is Loading -> Loading
            is Success -> Success(block(data))
            is None -> None
        }
    }

    fun getDataOrNull() = if (this is Success) data else null

    inline fun doOnError(block: (errorBody: ErrorBodyUI) -> Unit): UIState<T> {
        if (this is Error) {
            block(errorBody)
        }
        return this
    }

    inline fun doOnLoading(block: () -> Unit): UIState<T> {
        if (this is Loading) {
            block()
        }
        return this
    }

    inline fun doOnNone(block: () -> Unit): UIState<T> {
        if (this is None) {
            block()
        }
        return this
    }

    inline fun doOnSuccess(block: (T) -> Unit): UIState<T> {
        if (this is Success) {
            block(data)
        }
        return this
    }
}

fun <T> MutableStateFlow<UIState<T>>.updateIfSuccess(transform: (T) -> T) {
    this.update { state ->
        when (state) {
            is UIState.Success -> UIState.Success(transform(state.data))
            else -> state
        }
    }
}

fun <T> Flow<UIState<T>>.onSuccess(action: suspend (UIState.Success<T>) -> Unit): Flow<UIState<T>> =
    transform { value ->
        if (value is UIState.Success) {
            action(value)
        }
        return@transform emit(value)
    }

inline fun <reified T> UIState<T>.requireSuccess(): T {
    return when (this) {
        is UIState.Success -> data
        else -> error("Expected Success, but was $this")
    }
}

data class ErrorBodyUI(
    val info: DialogInfo,
    val error: ApiUnifiedErrorUI
)