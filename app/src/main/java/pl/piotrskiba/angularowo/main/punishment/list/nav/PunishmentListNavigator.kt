package pl.piotrskiba.angularowo.main.punishment.list.nav

import android.view.View
import pl.piotrskiba.angularowo.main.punishment.model.PunishmentBannerData

interface PunishmentListNavigator {

    fun onPunishmentClick(view: View, punishment: PunishmentBannerData)
}
