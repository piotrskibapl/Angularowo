package pl.piotrskiba.angularowo.domain.player.model

import pl.piotrskiba.angularowo.domain.rank.model.RankModel
import java.io.Serializable

data class DetailedPlayerModel(
    val uuid: String,
    val skinUuid: String,
    val partnerUuid: String?,
    val username: String,
    val status: String,
    val rank: RankModel,
    val isVanished: Boolean,
    val balance: Float,
    val playtime: Long,
    val tokens: Int,
    val permissions: List<PermissionModel>,
) : Serializable
