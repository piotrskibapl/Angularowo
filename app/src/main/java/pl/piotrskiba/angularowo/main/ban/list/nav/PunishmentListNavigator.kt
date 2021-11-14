package pl.piotrskiba.angularowo.main.ban.list.nav

import android.view.View
import pl.piotrskiba.angularowo.main.ban.model.PunishmentBannerData

interface PunishmentListNavigator {

    fun onPunishmentClick(view: View, punishment: PunishmentBannerData)
}
