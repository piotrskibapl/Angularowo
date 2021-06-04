package pl.piotrskiba.angularowo.main.base.viewmodel

import androidx.lifecycle.MutableLiveData
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayer
import javax.inject.Inject

class MainViewModel @Inject constructor() : LifecycleViewModel() {

    val player = MutableLiveData<DetailedPlayer>()
}