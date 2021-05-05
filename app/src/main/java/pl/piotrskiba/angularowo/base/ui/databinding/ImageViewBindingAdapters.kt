package pl.piotrskiba.angularowo.base.ui.databinding

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import pl.piotrskiba.angularowo.IntegerVersionSignature
import pl.piotrskiba.angularowo.utils.GlideUtils

@BindingAdapter("imageUrl", "placeholderImage", requireAll = false)
fun loadImage(
    view: ImageView,
    url: String,
    @DrawableRes placeholder: Int?
) {
    if (url.isNotEmpty()) {
        Glide.with(view.context)
            .load(url)
            .signature(IntegerVersionSignature(GlideUtils.getSignatureVersionNumber(5)))
            .placeholder(placeholder ?: 0)
            .into(view)
    }
}
