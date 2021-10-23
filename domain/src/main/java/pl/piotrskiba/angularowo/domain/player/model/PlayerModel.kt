package pl.piotrskiba.angularowo.domain.player.model

import pl.piotrskiba.angularowo.domain.rank.model.Rank

data class PlayerModel(
    val uuid: String,
    val skinUuid: String,
    val partnerUuid: String?,
    val username: String,
    var rank: Rank,
    val isVanished: Boolean
)
