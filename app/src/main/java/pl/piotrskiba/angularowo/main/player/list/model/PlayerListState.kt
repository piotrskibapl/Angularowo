package pl.piotrskiba.angularowo.main.player.list.model

import pl.piotrskiba.angularowo.main.player.model.PlayerBannerData

sealed class PlayerListState(open val bannerDataList: List<PlayerBannerData>) {
    object Loading : PlayerListState(emptyList())
    data class Loaded(override val bannerDataList: List<PlayerBannerData>) :
        PlayerListState(bannerDataList)
}