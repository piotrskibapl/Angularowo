package pl.piotrskiba.angularowo.models

import com.google.gson.annotations.SerializedName
import pl.piotrskiba.angularowo.utils.RankUtils
import java.io.Serializable

data class Player(
        val uuid: String,
        val skinUuid: String,
        val username: String,
        @SerializedName("rank") val rankKey: String,
        @SerializedName(value = "vanished") val isVanished: Boolean
) : Serializable {

    val rank: Rank
        get() = RankUtils.getRankFromName(rankKey)

}