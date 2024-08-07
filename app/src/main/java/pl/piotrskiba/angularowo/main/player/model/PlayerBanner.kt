package pl.piotrskiba.angularowo.main.player.model

import android.content.Context
import androidx.annotation.ColorInt
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.domain.player.model.PlayerModel
import pl.piotrskiba.angularowo.main.base.MinecraftColor
import java.io.Serializable

data class PlayerBanner(
    val uuid: String,
    val skinUuid: String,
    val username: String,
    val rankName: String,
    val isVanished: Boolean,
    val isFavorite: Boolean,
    private val rankColorCode: String,
) : Serializable {

    @ColorInt
    fun rankColor(context: Context) =
        context.getColor(MinecraftColor.colorCode[rankColorCode] ?: MinecraftColor.default)
}

fun PlayerModel.toPlayerBannerData(isFavorite: Boolean) = PlayerBanner(
    uuid,
    skinUuid,
    username,
    rank.name,
    isVanished,
    isFavorite,
    rank.colorCode,
)

fun DetailedPlayerModel.toPlayerBannerData(isFavorite: Boolean) = PlayerBanner(
    uuid,
    skinUuid,
    username,
    rank.name,
    isVanished,
    isFavorite,
    rank.colorCode,
)
