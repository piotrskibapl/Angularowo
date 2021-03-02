package pl.piotrskiba.angularowo.data.player.mapper

import pl.piotrskiba.angularowo.data.player.model.DetailedPlayerData
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayer
import pl.piotrskiba.angularowo.domain.rank.model.Rank

class DetailedPlayerMapper {

    fun toDetailedPlayer(data: DetailedPlayerData) = DetailedPlayer(
        data.uuid,
        data.skinUuid,
        data.partnerUuid,
        data.username,
        data.status,
        Rank.UnknownRank(data.rankName),
        data.isVanished,
        data.balance,
        data.playtime,
        data.tokens,
        data.permissions
    )
}