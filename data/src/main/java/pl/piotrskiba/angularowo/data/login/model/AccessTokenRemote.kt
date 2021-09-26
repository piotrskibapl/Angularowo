package pl.piotrskiba.angularowo.data.login.model

import com.google.gson.annotations.SerializedName
import pl.piotrskiba.angularowo.domain.login.model.AccessToken

data class AccessTokenRemote(
    val uuid: String? = null,
    val username: String? = null,
    @SerializedName("access_token")
    val accessToken: String? = null,
    val message: String? = null
)

fun AccessTokenRemote.toDomain() = AccessToken(
    uuid ?: "",
    username ?: "",
    accessToken ?: "",
    message ?: ""
)
