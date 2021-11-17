package pl.piotrskiba.angularowo.main.punishment.details.viewmodel

import androidx.lifecycle.MutableLiveData
import com.snakydesign.livedataextensions.map
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.main.punishment.details.DetailedPunishmentData
import javax.inject.Inject

class PunishmentDetailsViewModel @Inject constructor(
) : LifecycleViewModel() {

    val previewedPunishmentData: MutableLiveData<DetailedPunishmentData> = MutableLiveData()
    val previewedPunishmentBanner = previewedPunishmentData.map { it.toPunishmentBannerData() }
}
