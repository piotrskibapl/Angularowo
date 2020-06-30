package pl.piotrskiba.angularowo.models

import pl.piotrskiba.angularowo.utils.RankUtils
import java.io.Serializable

class Player(val uuid: String?, val username: String, private val rank: String, val vanished: Boolean) : Serializable {

    fun getRank(): Rank {
        return RankUtils.getRankFromName(rank)
    }

}