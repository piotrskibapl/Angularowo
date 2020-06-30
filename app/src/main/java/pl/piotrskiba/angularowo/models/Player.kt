package pl.piotrskiba.angularowo.models

import pl.piotrskiba.angularowo.database.entity.Friend
import pl.piotrskiba.angularowo.utils.RankUtils
import java.io.Serializable

data class Player(
        val uuid: String?,
        val username: String,
        val rankKey: String,
        val vanished: Boolean
) : Serializable {

    val rank: Rank
        get() = RankUtils.getRankFromName(rankKey)

    fun asFriend() = Friend(uuid, username, rankKey, vanished)

}