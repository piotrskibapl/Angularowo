package pl.piotrskiba.angularowo.domain.login.model

import retrofit2.HttpException

sealed class AccessTokenError : Throwable() {
    object CodeNotFoundError : AccessTokenError()
    object CodeExpiredError : AccessTokenError()
    object UnknownError : AccessTokenError()
}

fun Throwable.toAccessTokenError(): AccessTokenError = when (this) {
    is HttpException -> this.toAccessTokenError()
    else -> AccessTokenError.UnknownError
}

private fun HttpException.toAccessTokenError(): AccessTokenError = when (code()) {
    403 -> AccessTokenError.CodeExpiredError
    404 -> AccessTokenError.CodeNotFoundError
    else -> AccessTokenError.UnknownError
}
