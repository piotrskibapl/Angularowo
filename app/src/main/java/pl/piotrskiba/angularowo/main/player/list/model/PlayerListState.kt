package pl.piotrskiba.angularowo.main.player.list.model

sealed class PlayerListState {
    object Loading : PlayerListState()
    object Loaded : PlayerListState()
}
