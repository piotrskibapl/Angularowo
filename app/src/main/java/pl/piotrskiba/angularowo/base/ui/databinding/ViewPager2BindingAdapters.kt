package pl.piotrskiba.angularowo.base.ui.databinding

import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2

@BindingAdapter("userInputEnabled")
fun setUserInputEnabled(
    view: ViewPager2,
    enabled: Boolean
) {
    view.isUserInputEnabled = enabled
}
