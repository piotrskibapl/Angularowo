package pl.piotrskiba.angularowo.data.player.model

import com.google.gson.annotations.SerializedName

data class PlayerData(
    val uuid: String,
    val skinUuid: String,
    val partnerUuid: String?,
    val username: String,
    @SerializedName("rank") val rankName: String,
    @SerializedName(value = "vanished") val isVanished: Boolean
)
