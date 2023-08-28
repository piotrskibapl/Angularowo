package pl.piotrskiba.angularowo.main.punishment.details.viewmodel

import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.main.punishment.details.ui.PunishmentDetailsFragmentArgs
import javax.inject.Inject

class PunishmentDetailsViewModel @Inject constructor() : LifecycleViewModel() {

    lateinit var args: PunishmentDetailsFragmentArgs
    val previewedPunishmentData by lazy { args.punishment }
    val previewedPunishmentBanner by lazy { args.punishment.toPunishmentBanner() }
}
