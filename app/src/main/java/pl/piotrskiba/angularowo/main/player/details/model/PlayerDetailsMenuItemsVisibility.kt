package pl.piotrskiba.angularowo.main.player.details.model

import pl.piotrskiba.angularowo.domain.player.model.PlayerDetailsMenuItemsVisibilityModel

data class PlayerDetailsMenuItemsVisibility(
    val favorite: Boolean,
    val unfavorite: Boolean,
    val mute: Boolean,
    val kick: Boolean,
    val warn: Boolean,
    val ban: Boolean,
)

fun PlayerDetailsMenuItemsVisibilityModel.toUi() =
    PlayerDetailsMenuItemsVisibility(
        favorite = favorite,
        unfavorite = unfavorite,
        mute = mute,
        kick = kick,
        warn = warn,
        ban = ban,
    )
