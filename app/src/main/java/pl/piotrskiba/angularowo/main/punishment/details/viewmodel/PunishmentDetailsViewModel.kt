package pl.piotrskiba.angularowo.main.punishment.details.viewmodel

import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.base.extensions.serializable
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.main.punishment.details.DetailedPunishmentData
import pl.piotrskiba.angularowo.main.punishment.model.PunishmentBannerData
import javax.inject.Inject

class PunishmentDetailsViewModel @Inject constructor(
) : LifecycleViewModel() {

    val previewedPunishmentData: DetailedPunishmentData by lazy { intent.serializable(Constants.EXTRA_PUNISHMENT)!! }
    val previewedPunishmentBanner: PunishmentBannerData by lazy {
        intent.serializable<DetailedPunishmentData>(Constants.EXTRA_PUNISHMENT)!!.toPunishmentBannerData()
    }
}
