package pl.piotrskiba.angularowo.base.model

sealed class ViewModelState {
    object Loading : ViewModelState()
    object Loaded : ViewModelState()
    object Error: ViewModelState()
}
