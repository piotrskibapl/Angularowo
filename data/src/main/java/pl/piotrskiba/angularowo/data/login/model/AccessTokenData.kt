package pl.piotrskiba.angularowo.data.login.model

import com.google.gson.annotations.SerializedName

data class AccessTokenData(
    val uuid: String? = null,
    val username: String? = null,
    @SerializedName("access_token")
    val accessToken: String? = null,
    val message: String? = null
)
