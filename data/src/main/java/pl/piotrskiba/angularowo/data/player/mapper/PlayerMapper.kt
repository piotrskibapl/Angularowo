package pl.piotrskiba.angularowo.data.player.mapper

import pl.piotrskiba.angularowo.data.player.model.PlayerData
import pl.piotrskiba.angularowo.domain.player.model.Player
import pl.piotrskiba.angularowo.domain.rank.model.Rank

class PlayerMapper {

    fun toPlayer(data: PlayerData) = Player(
        data.uuid,
        data.skinUuid,
        data.partnerUuid,
        data.username,
        Rank.UnknownRank(data.rankName),
        data.isVanished
    )
}
