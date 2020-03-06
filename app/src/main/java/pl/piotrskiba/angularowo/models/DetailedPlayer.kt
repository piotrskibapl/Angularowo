package pl.piotrskiba.angularowo.models

import pl.piotrskiba.angularowo.utils.RankUtils

class DetailedPlayer(val username: String, val uuid: String, val status: String, private val rank: String, val isVanished: Boolean, val balance: Float, val playtime: Int, val tokens: Int) {

    fun getRank(): Rank {
        return RankUtils.getRankFromName(rank)
    }

}