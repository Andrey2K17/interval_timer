package ru.grinin.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import retrofit2.Response
import ru.grinin.domain.entities.ErrorBody
import ru.grinin.domain.entities.NetworkResult

internal inline fun <ResponseType, ResultType> remoteCall(
    crossinline call: suspend () -> Response<ResponseType>,
    crossinline mapper: (ResponseType) -> ResultType,
    apiErrorHandlerImpl: ApiErrorHandlerImpl = ApiErrorHandlerImpl()
): Flow<NetworkResult<ResultType>> = flow {
    emit(NetworkResult.Loading())

    val response = call()
    if (response.isSuccessful) {
        val body = response.body()
        body?.let {
            emit(NetworkResult.Success(mapper(it)))
        }
    } else {
        val httpException = HttpException(response)
        val ex = apiErrorHandlerImpl.invoke(httpException)
        val errorBody = ErrorBody(ex, httpException)

        emit(NetworkResult.Error(errorBody))
    }
}.flowOn(Dispatchers.IO).catch { throwable: Throwable ->
    val error = apiErrorHandlerImpl.invoke(throwable)
    val errorBody = ErrorBody(error, throwable)

    emit(NetworkResult.Error(errorBody))
}