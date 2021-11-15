package pl.piotrskiba.angularowo.main.punishment.details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.github.magneticflux.livedata.map
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.main.punishment.details.DetailedPunishmentData
import pl.piotrskiba.angularowo.main.punishment.model.PunishmentBannerData
import javax.inject.Inject

class PunishmentDetailsViewModel @Inject constructor(
) : LifecycleViewModel() {

    val previewedPunishmentData: MutableLiveData<DetailedPunishmentData> = MutableLiveData()
    val previewedPunishmentBanner: LiveData<PunishmentBannerData> =
        previewedPunishmentData.map { it.toPunishmentBannerData() }

}
