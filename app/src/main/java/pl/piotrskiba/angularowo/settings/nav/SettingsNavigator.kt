package pl.piotrskiba.angularowo.settings.nav

interface SettingsNavigator {
    fun displayLogoutConfirmationDialog(onConfirm: () -> Unit)
    fun closeActivity()
}
