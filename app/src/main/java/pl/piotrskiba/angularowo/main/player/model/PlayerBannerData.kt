package pl.piotrskiba.angularowo.main.player.model

import android.view.View
import pl.piotrskiba.angularowo.domain.player.model.Player

data class PlayerBannerData(
    val skinUuid: String,
    val username: String,
    val rankName: String,
    private val isVanished: Boolean
) {

    fun vanishIconVisibility() = when (isVanished) {
        true -> View.VISIBLE
        false -> View.GONE
    }
}

fun Player.toUi() = PlayerBannerData(
    skinUuid,
    username,
    rank.name,
    isVanished
)