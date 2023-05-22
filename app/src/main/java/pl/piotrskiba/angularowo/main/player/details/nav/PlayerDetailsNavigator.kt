package pl.piotrskiba.angularowo.main.player.details.nav

import pl.piotrskiba.angularowo.main.player.details.model.PunishmentType

interface PlayerDetailsNavigator {

    fun displayMarkedAsFavoriteSnackbar()
    fun displayUnmarkedAsFavoriteSnackbar()
    fun displayGenericErrorSnackbar()
    fun displayFavoriteShowcase()
    fun displayPunishmentSuccessDialog(type: PunishmentType)
    fun displayPunishmentErrorDialog()
}
