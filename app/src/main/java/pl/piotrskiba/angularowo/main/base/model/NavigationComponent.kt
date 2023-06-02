package pl.piotrskiba.angularowo.main.base.model

import pl.piotrskiba.angularowo.R

object NavigationComponent {

    val topLevelDestinations =
        setOf(
            R.id.initFragment,
            R.id.mainScreenFragment,
            R.id.playerListFragment,
            R.id.chatFragment,
            R.id.lastPunishmentsFragment,
            R.id.offersFragment,
            R.id.reportListContainerFragment,
        )

    val noToolbarNavigationIconDestinations =
        setOf(
            R.id.loginFragment,
            R.id.appLockFragment,
        )
}
