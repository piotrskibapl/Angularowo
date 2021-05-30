package pl.piotrskiba.angularowo.main.player.list.nav

import pl.piotrskiba.angularowo.main.player.model.PlayerBannerData

interface PlayerListNavigator {

    fun onPlayerClick(player: PlayerBannerData)
}
