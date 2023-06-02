package pl.piotrskiba.angularowo.main.player.details.model

import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.domain.player.model.PermissionModel
import pl.piotrskiba.angularowo.main.player.model.PlayerBanner

data class PlayerDetailsMenuItemsVisibility(
    val favorite: Boolean,
    val unfavorite: Boolean,
    val mute: Boolean,
    val kick: Boolean,
    val warn: Boolean,
    val ban: Boolean,
) {

    companion object {

        // TODO: nullable previewedPlayerBanner is only a temporary solution
        fun from(player: DetailedPlayerModel, previewedPlayerBanner: PlayerBanner?) =
            if (previewedPlayerBanner == null) {
                PlayerDetailsMenuItemsVisibility(favorite = false, unfavorite = false, mute = false, kick = false, warn = false, ban = false)
            } else {
                PlayerDetailsMenuItemsVisibility(
                    favorite = !previewingSelfOrPartner(player, previewedPlayerBanner) && !previewedPlayerBanner.isFavorite,
                    unfavorite = !previewingSelfOrPartner(player, previewedPlayerBanner) && previewedPlayerBanner.isFavorite,
                    mute = player.permissions.contains(PermissionModel.MUTE_PLAYERS),
                    kick = player.permissions.contains(PermissionModel.KICK_PLAYERS),
                    warn = player.permissions.contains(PermissionModel.WARN_PLAYERS),
                    ban = player.permissions.contains(PermissionModel.BAN_PLAYERS),
                )
            }

        private fun previewingSelfOrPartner(player: DetailedPlayerModel, previewedPlayerBanner: PlayerBanner) =
            player.uuid == previewedPlayerBanner.uuid || player.partnerUuid == previewedPlayerBanner.uuid
    }
}
