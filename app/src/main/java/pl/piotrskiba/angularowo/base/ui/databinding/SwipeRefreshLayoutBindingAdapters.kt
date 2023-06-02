package pl.piotrskiba.angularowo.base.ui.databinding

import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import pl.piotrskiba.angularowo.base.model.ViewModelState

@BindingAdapter("state")
fun setState(
    view: SwipeRefreshLayout,
    state: ViewModelState,
) {
    view.isRefreshing = state.isRefreshing()
    view.isEnabled = !state.fullscreenLoaderVisible()
}
