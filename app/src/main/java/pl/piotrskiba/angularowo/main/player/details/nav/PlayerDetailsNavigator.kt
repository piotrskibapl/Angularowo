package pl.piotrskiba.angularowo.main.player.details.nav

interface PlayerDetailsNavigator {

    fun displayMarkedAsFavoriteSnackbar()
    fun displayUnmarkedAsFavoriteSnackbar()
    fun displayGenericErrorSnackbar()
    fun displayFavoriteShowcase()
}
