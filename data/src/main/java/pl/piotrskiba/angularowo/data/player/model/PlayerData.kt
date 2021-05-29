package pl.piotrskiba.angularowo.data.player.model

import com.google.gson.annotations.SerializedName
import pl.piotrskiba.angularowo.domain.player.model.Player
import pl.piotrskiba.angularowo.domain.rank.model.Rank.UnknownRank

data class PlayerData(
    val uuid: String,
    val skinUuid: String,
    val partnerUuid: String?,
    val username: String,
    @SerializedName("rank") val rankName: String,
    @SerializedName(value = "vanished") val isVanished: Boolean
)

fun PlayerData.toDomain() = Player(
    uuid,
    skinUuid,
    partnerUuid,
    username,
    UnknownRank(rankName),
    isVanished
)
