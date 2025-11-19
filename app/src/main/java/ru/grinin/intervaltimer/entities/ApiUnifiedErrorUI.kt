package ru.grinin.intervaltimer.entities

import ru.grinin.domain.entities.ApiUnifiedError

sealed class ApiUnifiedErrorUI(open val message: String?, open val code: Int?) {
    data class GenericUI(override val message: String?) : ApiUnifiedErrorUI(message, null)

    sealed class HttpUI(message: String?, errorCode: Int) :
        ApiUnifiedErrorUI(message, errorCode) {
        data class UnauthorizedUI(override val message: String?, val errorCode: Int) :
            HttpUI(message, errorCode)

        data class NotFoundUI(override val message: String?, val errorCode: Int) :
            HttpUI(message, errorCode)

        data class InternalErrorUI(override val message: String?, val errorCode: Int) :
            HttpUI(message, errorCode)

        data class BadRequestUI(override val message: String?, val errorCode: Int) :
            HttpUI(message, errorCode)

        data class RequireHigherApiVersionErrorUI(
            override val message: String?,
            val errorCode: Int
        ) :
            HttpUI(message, errorCode)
    }

    sealed class ConnectivityUI(message: String?) : ApiUnifiedErrorUI(message, null) {
        data class HostUnreachableUI(override val message: String?) :
            ConnectivityUI(message)

        data class TimeOutUI(override val message: String?) : ConnectivityUI(message)
        data class NoConnectionUI(override val message: String?) : ConnectivityUI(message)
    }
}

fun ApiUnifiedErrorUI.getLocalMessage(): String =
     when (this) {
        is ApiUnifiedErrorUI.ConnectivityUI.HostUnreachableUI -> "There is no connection to the internet, please try again later."
        is ApiUnifiedErrorUI.ConnectivityUI.NoConnectionUI -> "There is no connection to the internet, please try again later."
        is ApiUnifiedErrorUI.ConnectivityUI.TimeOutUI -> "Looks like you have an unstable network at the moment, please try again when network stabilizes."
        is ApiUnifiedErrorUI.GenericUI -> this.message ?: "An unexpected error occurred, please try again later."
        is ApiUnifiedErrorUI.HttpUI.BadRequestUI -> "An unexpected error occurred when contacting the server, please try again later."
        is ApiUnifiedErrorUI.HttpUI.InternalErrorUI -> "An unexpected error occurred when contacting the server, please try again later."
        is ApiUnifiedErrorUI.HttpUI.NotFoundUI -> "An unexpected error occurred when contacting the server, please try again later."
        is ApiUnifiedErrorUI.HttpUI.UnauthorizedUI -> this.message ?: "Unauthorized"
         is ApiUnifiedErrorUI.HttpUI.RequireHigherApiVersionErrorUI -> this.message ?: "Download the latest version"
     }

fun ApiUnifiedError.mapToUI(): ApiUnifiedErrorUI {
    return when (this) {
        is ApiUnifiedError.Http -> {
            when (this) {
                is ApiUnifiedError.Http.InternalError -> ApiUnifiedErrorUI.HttpUI.InternalErrorUI(
                    message,
                    errorCode
                )

                is ApiUnifiedError.Http.BadRequest -> ApiUnifiedErrorUI.HttpUI.BadRequestUI(
                    message,
                    errorCode
                )

                is ApiUnifiedError.Http.NotFound -> ApiUnifiedErrorUI.HttpUI.NotFoundUI(
                    message,
                    errorCode
                )

                is ApiUnifiedError.Http.Unauthorized -> ApiUnifiedErrorUI.HttpUI.UnauthorizedUI(
                    message,
                    errorCode
                )
            }
        }

        is ApiUnifiedError.Generic -> ApiUnifiedErrorUI.GenericUI(this.message)
        is ApiUnifiedError.Connectivity -> {
            when (this) {
                is ApiUnifiedError.Connectivity.HostUnreachable -> ApiUnifiedErrorUI.ConnectivityUI.HostUnreachableUI(
                    message
                )

                is ApiUnifiedError.Connectivity.NoConnection -> ApiUnifiedErrorUI.ConnectivityUI.NoConnectionUI(
                    message
                )

                is ApiUnifiedError.Connectivity.TimeOut -> ApiUnifiedErrorUI.ConnectivityUI.TimeOutUI(
                    message
                )
            }
        }
    }
}