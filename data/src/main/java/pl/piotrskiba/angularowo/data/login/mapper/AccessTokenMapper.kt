package pl.piotrskiba.angularowo.data.login.mapper

import pl.piotrskiba.angularowo.data.login.model.AccessTokenData
import pl.piotrskiba.angularowo.domain.login.model.AccessToken

class AccessTokenMapper {

    fun toAccessToken(data: AccessTokenData) = AccessToken(
        data.uuid ?: "",
        data.username ?: "",
        data.accessToken ?: "",
        data.message ?: ""
    )
}