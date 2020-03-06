package pl.piotrskiba.angularowo.models

import com.google.gson.annotations.SerializedName

class AccessToken(
        val username: String,
        @field:SerializedName("access_token")
        val accessToken: String,
        val message: String)