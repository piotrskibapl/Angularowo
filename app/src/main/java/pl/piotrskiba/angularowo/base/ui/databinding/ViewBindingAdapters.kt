package pl.piotrskiba.angularowo.base.ui.databinding

import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter

@BindingAdapter("backgroundColor")
fun setBackgroundColor(
    view: View,
    @ColorRes colorResId: Int
) {
    view.setBackgroundColor(
        ContextCompat.getColor(view.context, colorResId)
    )
}

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
