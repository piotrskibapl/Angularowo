package pl.piotrskiba.angularowo.base.ui.databinding

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun setVisibility(
    view: View,
    visible: Boolean
) {
    view.visibility = when (visible) {
        true -> View.VISIBLE
        false -> View.GONE
    }
}
