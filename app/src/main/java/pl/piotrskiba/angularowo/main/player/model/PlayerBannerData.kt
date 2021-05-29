package pl.piotrskiba.angularowo.main.player.model

import android.content.Context
import android.view.View
import androidx.annotation.ColorRes
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.player.model.Player
import pl.piotrskiba.angularowo.utils.UrlUtils

private const val COLOR_CODE_RES_PREFIX = "color_minecraft_"

data class PlayerBannerData(
    val skinUuid: String,
    val username: String,
    val rankName: String,
    val rankColorCode: String,
    private val isVanished: Boolean
) {

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

    fun vanishIconVisibility() = when (isVanished) {
        true -> View.VISIBLE
        false -> View.GONE
    }

    fun avatarUrl() = UrlUtils.buildAvatarUrl(skinUuid, true)
}

fun Player.toUi() = PlayerBannerData(
    skinUuid,
    username,
    rank.name,
    rank.colorCode,
    isVanished
)