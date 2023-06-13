package pl.piotrskiba.angularowo.data.login.model

import pl.piotrskiba.angularowo.domain.login.model.AccessTokenModel

data class AccessTokenRemote(
    val uuid: String? = null,
    val username: String? = null,
    val access_token: String? = null,
    val message: String? = null,
)

fun AccessTokenRemote.toDomain() = AccessTokenModel(
    uuid ?: "",
    username ?: "",
    access_token ?: "",
    message ?: "",
)
