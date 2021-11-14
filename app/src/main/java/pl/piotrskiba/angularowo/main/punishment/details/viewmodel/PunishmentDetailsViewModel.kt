package pl.piotrskiba.angularowo.main.punishment.details.viewmodel

import androidx.lifecycle.MutableLiveData
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.main.punishment.details.DetailedPunishmentData
import pl.piotrskiba.angularowo.main.punishment.model.PunishmentBannerData
import javax.inject.Inject

class PunishmentDetailsViewModel @Inject constructor(
) : LifecycleViewModel() {

    val previewedPunishmentBanner: MutableLiveData<DetailedPunishmentData> = MutableLiveData()

}
