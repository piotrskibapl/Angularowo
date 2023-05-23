package pl.piotrskiba.angularowo.data.player.model

import com.google.gson.annotations.SerializedName
import pl.piotrskiba.angularowo.domain.player.model.PlayerModel
import pl.piotrskiba.angularowo.domain.rank.model.RankModel

data class PlayerRemote(
    val uuid: String,
    val skinUuid: String,
    val partnerUuid: String?,
    val username: String,
    @SerializedName("rank") val rankName: String,
    @SerializedName(value = "vanished") val isVanished: Boolean,
)

fun PlayerRemote.toDomain() = PlayerModel(
    uuid,
    skinUuid,
    partnerUuid,
    username,
    RankModel.unknownRank(rankName),
    isVanished,
)
