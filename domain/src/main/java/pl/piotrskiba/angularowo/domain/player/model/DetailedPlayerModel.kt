package pl.piotrskiba.angularowo.domain.player.model

import pl.piotrskiba.angularowo.domain.rank.model.Rank
import java.io.Serializable

data class DetailedPlayerModel(
    val uuid: String,
    val skinUuid: String,
    val partnerUuid: String?,
    val username: String,
    val status: String,
    var rank: Rank,
    val isVanished: Boolean,
    val balance: Float,
    val playtime: Int,
    val tokens: Int,
    val permissions: List<String>
) : Serializable