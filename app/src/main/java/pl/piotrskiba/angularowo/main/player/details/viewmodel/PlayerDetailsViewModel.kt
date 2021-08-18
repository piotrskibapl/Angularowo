package pl.piotrskiba.angularowo.main.player.details.viewmodel

import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.main.player.model.PlayerBannerData
import javax.inject.Inject

class PlayerDetailsViewModel @Inject constructor() : LifecycleViewModel() {
    lateinit var previewedPlayer: PlayerBannerData
}
