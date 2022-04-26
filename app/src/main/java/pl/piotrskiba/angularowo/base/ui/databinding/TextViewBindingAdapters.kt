package pl.piotrskiba.angularowo.base.ui.databinding

import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

@BindingAdapter(
    "drawableLeft",
    "drawableTop",
    "drawableRight",
    "drawableBottom",
    requireAll = false
)
fun setDrawables(
    textView: TextView,
    @DrawableRes drawableLeft: Int = 0,
    @DrawableRes drawableTop: Int = 0,
    @DrawableRes drawableRight: Int = 0,
    @DrawableRes drawableBottom: Int = 0,
) {
    textView.setCompoundDrawablesWithIntrinsicBounds(
        drawableLeft,
        drawableTop,
        drawableRight,
        drawableBottom
    )
}
