package pl.piotrskiba.angularowo.settings.nav

interface SettingsNavigator {
    fun onLogoutClicked(successCallback: () -> Unit)
}
