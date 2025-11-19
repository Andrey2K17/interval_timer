package ru.grinin.data.network

import retrofit2.HttpException
import ru.grinin.domain.entities.ApiUnifiedError
import java.io.IOException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.SocketTimeoutException
import java.net.UnknownHostException

interface IApiErrorHandler {
    fun invoke(t: Throwable): ApiUnifiedError
}

class ApiErrorHandlerImpl : IApiErrorHandler {
    override fun invoke(t: Throwable): ApiUnifiedError =
        when (t) {
            is IOException -> t.handleError()
            is HttpException -> t.handleError()
            else -> ApiUnifiedError.Generic(t.message)
        }

    private fun HttpException.handleError(): ApiUnifiedError =
        when (val code = code()) {
            HttpURLConnection.HTTP_UNAUTHORIZED ->
                ApiUnifiedError.Http.Unauthorized(message = "Unauthorized", code)

            HttpURLConnection.HTTP_NOT_FOUND ->
                ApiUnifiedError.Http.NotFound(message = "Not Found", code)

            HttpURLConnection.HTTP_INTERNAL_ERROR ->
                ApiUnifiedError.Http.InternalError(message = "Internal Error", code)

            HttpURLConnection.HTTP_BAD_REQUEST ->
                ApiUnifiedError.Http.BadRequest(message = "Bad Request", code)

            else -> ApiUnifiedError.Generic(message = message)
        }

    private fun IOException.handleError(): ApiUnifiedError =
        when (this) {
            is SocketTimeoutException -> ApiUnifiedError.Connectivity.TimeOut(message)
            is ConnectException -> ApiUnifiedError.Connectivity.NoConnection(message)
            is UnknownHostException -> ApiUnifiedError.Connectivity.HostUnreachable(message)
            else -> ApiUnifiedError.Generic(message)
        }
}