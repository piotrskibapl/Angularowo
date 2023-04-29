package pl.piotrskiba.angularowo.domain.player.model

import pl.piotrskiba.angularowo.domain.rank.model.RankModel

data class PlayerModel(
    val uuid: String,
    val skinUuid: String,
    val partnerUuid: String?,
    val username: String,
    val rank: RankModel,
    val isVanished: Boolean
)
