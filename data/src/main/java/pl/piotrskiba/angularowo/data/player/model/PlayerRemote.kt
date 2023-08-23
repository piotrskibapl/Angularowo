package pl.piotrskiba.angularowo.data.player.model

import pl.piotrskiba.angularowo.domain.player.model.PlayerModel
import pl.piotrskiba.angularowo.domain.rank.model.RankModel

data class PlayerRemote(
    val uuid: String,
    val skinUuid: String,
    val partnerUuid: String?,
    val username: String,
    val rank: String,
    val vanished: Boolean,
)

fun List<PlayerRemote>.toDomain() = map { it.toDomain() }

private fun PlayerRemote.toDomain() = PlayerModel(
    uuid,
    skinUuid,
    partnerUuid,
    username,
    RankModel.unknownRank(rank),
    vanished,
)
