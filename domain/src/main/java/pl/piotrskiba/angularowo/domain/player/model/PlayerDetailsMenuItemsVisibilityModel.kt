package pl.piotrskiba.angularowo.domain.player.model

data class PlayerDetailsMenuItemsVisibilityModel(
    val favorite: Boolean,
    val unfavorite: Boolean,
    val mute: Boolean,
    val kick: Boolean,
    val warn: Boolean,
    val ban: Boolean,
)
