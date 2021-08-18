package pl.piotrskiba.angularowo.main.player.details.viewmodel

import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayer
import pl.piotrskiba.angularowo.main.player.model.PlayerBannerData
import javax.inject.Inject

class PlayerDetailsViewModel @Inject constructor() : LifecycleViewModel() {

    lateinit var player: DetailedPlayer
    lateinit var previewedPlayer: PlayerBannerData
}
