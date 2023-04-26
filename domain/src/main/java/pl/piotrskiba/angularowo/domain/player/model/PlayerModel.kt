package pl.piotrskiba.angularowo.domain.player.model

import pl.piotrskiba.angularowo.domain.rank.model.Rank

data class PlayerModel(
    val uuid: String,
    val skinUuid: String,
    val partnerUuid: String?,
    val username: String,
    val rank: Rank,
    val isVanished: Boolean
)
