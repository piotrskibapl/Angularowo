package pl.piotrskiba.angularowo.data.login.model

import pl.piotrskiba.angularowo.domain.login.model.AccessTokenModel

data class AccessTokenRemote(
    val uuid: String,
    val username: String,
    val access_token: String,
    val message: String,
)

fun AccessTokenRemote.toDomain() = AccessTokenModel(
    uuid = uuid,
    username = username,
    accessToken = access_token,
    message = message,
)
