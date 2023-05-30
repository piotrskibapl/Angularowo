package pl.piotrskiba.angularowo.base.ui.databinding

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import pl.piotrskiba.angularowo.R

@BindingAdapter("visible")
fun setVisibility(
    view: View,
    visible: Boolean,
) {
    view.visibility = when (visible) {
        true -> View.VISIBLE
        false -> View.GONE
    }
}

@BindingAdapter(
    "displayChevron",
    "chevronColor",
)
fun setChevron(
    view: TextView,
    display: Boolean,
    color: Int?,
) {
    if (display) {
        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_right, 0)
        for (drawable in view.compoundDrawables) {
            drawable?.colorFilter = PorterDuffColorFilter(color!!, PorterDuff.Mode.SRC_IN)
        }
    } else {
        view.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
    }
}
