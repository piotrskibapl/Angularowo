package pl.piotrskiba.angularowo.models

import com.google.gson.annotations.SerializedName
import pl.piotrskiba.angularowo.utils.RankUtils

data class DetailedPlayer(
        val uuid: String?,
        val username: String,
        val status: String,
        @SerializedName(value = "rank") val rankKey: String,
        val isVanished: Boolean,
        val balance: Float,
        val playtime: Int,
        val tokens: Int
) {

    val rank: Rank
        get() = RankUtils.getRankFromName(rankKey)

}