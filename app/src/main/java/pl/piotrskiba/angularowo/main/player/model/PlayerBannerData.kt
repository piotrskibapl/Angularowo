package pl.piotrskiba.angularowo.main.player.model

import android.content.Context
import androidx.annotation.ColorRes
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayer
import pl.piotrskiba.angularowo.domain.player.model.Player
import pl.piotrskiba.angularowo.utils.UrlUtils
import java.io.Serializable

private const val COLOR_CODE_RES_PREFIX = "color_minecraft_"

data class PlayerBannerData(
    val uuid: String,
    val skinUuid: String,
    val username: String,
    val rankName: String,
    val isVanished: Boolean,
    private val rankColorCode: String
) : Serializable {

    @ColorRes
    fun rankColorResId(context: Context): Int {
        val colorResId = context.resources.getIdentifier(
            COLOR_CODE_RES_PREFIX + rankColorCode,
            "color",
            context.packageName
        )
        return when (colorResId) {
            0 -> R.color.color_minecraft_7
            else -> colorResId
        }
    }

    fun avatarUrl() = UrlUtils.buildAvatarUrl(skinUuid, true)
}

fun Player.toPlayerBannerData() = PlayerBannerData(
    uuid,
    skinUuid,
    username,
    rank.name,
    isVanished,
    rank.colorCode
)

fun DetailedPlayer.toPlayerBannerData() = PlayerBannerData(
    uuid,
    skinUuid,
    username,
    rank.name,
    isVanished,
    rank.colorCode
)
