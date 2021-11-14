package pl.piotrskiba.angularowo.main.ban.list.nav

import android.view.View
import pl.piotrskiba.angularowo.main.ban.model.BanBannerData

interface PunishmentListNavigator {

    fun onPunishmentClick(view: View, punishment: BanBannerData)
}
