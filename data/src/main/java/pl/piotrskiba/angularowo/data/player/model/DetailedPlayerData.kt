package pl.piotrskiba.angularowo.data.player.model

import com.google.gson.annotations.SerializedName

data class DetailedPlayerData(
    val uuid: String,
    val skinUuid: String,
    val partnerUuid: String?,
    val username: String,
    val status: String,
    @SerializedName(value = "rank") val rankKey: String,
    @SerializedName(value = "vanished") val isVanished: Boolean,
    val balance: Float,
    val playtime: Int,
    val tokens: Int,
    val permissions: List<String>
)
