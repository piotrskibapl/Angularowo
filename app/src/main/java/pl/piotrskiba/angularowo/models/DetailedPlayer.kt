package pl.piotrskiba.angularowo.models

import com.google.gson.annotations.SerializedName
import pl.piotrskiba.angularowo.utils.RankUtils
import java.io.Serializable

data class DetailedPlayer(
        val uuid: String,
        val skinUuid: String,
        val partnerUuid: String?,
        val username: String,
        val status: String,
        @SerializedName(value = "rank") val rankKey: String,
        @SerializedName(value = "vanished") val isVanished: Boolean,
        val balance: Float,
        val playtime: Int,
        val tokens: Int,
        private val permissions: List<String>
): Serializable {

    val rank: Rank
        get() = RankUtils.getRankFromName(rankKey)

    fun hasPermission(permission: String) = permissions.contains(permission)

}
