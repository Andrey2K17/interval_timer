package ru.grinin.intervaltimer.mappers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.grinin.domain.entities.NetworkResult
import ru.grinin.intervaltimer.entities.DialogInfo
import ru.grinin.intervaltimer.entities.ErrorBodyUI
import ru.grinin.intervaltimer.entities.UIState
import ru.grinin.intervaltimer.entities.getLocalMessage
import ru.grinin.intervaltimer.entities.mapToUI

inline fun <T, R> Flow<NetworkResult<T>>.mapToUIState(
    crossinline mapper: (T) -> R
): Flow<UIState<R>> = map { result ->
    when (result) {
        is NetworkResult.Success -> UIState.Success(mapper(result.data))

        is NetworkResult.Error -> {
            val mappedError = result.errorBody.error.mapToUI()
            UIState.Error(
                errorBody = ErrorBodyUI(
                    info = DialogInfo(
                        isShow = true,
                        text = mappedError.getLocalMessage().ifEmpty {
                            result.errorBody.fullErrorMessage
                        }
                    ),
                    error = mappedError
                )
            )
        }

        is NetworkResult.Loading -> UIState.Loading
    }
}