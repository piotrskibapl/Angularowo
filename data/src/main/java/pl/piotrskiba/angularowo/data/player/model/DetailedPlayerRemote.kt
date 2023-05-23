package pl.piotrskiba.angularowo.data.player.model

import com.google.gson.annotations.SerializedName
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.domain.player.model.PermissionModel
import pl.piotrskiba.angularowo.domain.rank.model.RankModel

data class DetailedPlayerRemote(
    val uuid: String,
    val skinUuid: String,
    val partnerUuid: String?,
    val username: String,
    val status: String,
    @SerializedName(value = "rank") val rankName: String,
    @SerializedName(value = "vanished") val isVanished: Boolean,
    val balance: Float,
    val playtime: Long,
    val tokens: Int,
    val permissions: List<String>,
)

fun DetailedPlayerRemote.toDomain() = DetailedPlayerModel(
    uuid,
    skinUuid,
    partnerUuid,
    username,
    status,
    RankModel.unknownRank(rankName),
    isVanished,
    balance,
    playtime,
    tokens,
    permissions.toPermissionModels(),
)

private fun List<String>.toPermissionModels() =
    mapNotNull { remote ->
        PermissionModel.values().firstOrNull { permission ->
            permission.name.equals(remote, ignoreCase = true)
        }
    }
