package pl.piotrskiba.angularowo.main.player.list.nav

import android.view.View
import pl.piotrskiba.angularowo.main.player.model.PlayerBannerData

interface PlayerListNavigator {

    fun onPlayerClick(view: View, player: PlayerBannerData)
}
