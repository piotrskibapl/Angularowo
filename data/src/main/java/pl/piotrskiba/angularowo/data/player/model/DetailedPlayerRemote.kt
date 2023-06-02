package pl.piotrskiba.angularowo.data.player.model

import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.domain.player.model.PermissionModel
import pl.piotrskiba.angularowo.domain.rank.model.RankModel

data class DetailedPlayerRemote(
    val uuid: String,
    val skinUuid: String,
    val partnerUuid: String?,
    val username: String,
    val status: String,
    val rank: String,
    val vanished: Boolean,
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
    RankModel.unknownRank(rank),
    vanished,
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
