package pl.piotrskiba.angularowo.settings.nav

interface SettingsNavigator {
    fun askForNotificationsPermission()
    fun displayLogoutConfirmationDialog(onConfirm: () -> Unit)
    fun displayLogin()
}
