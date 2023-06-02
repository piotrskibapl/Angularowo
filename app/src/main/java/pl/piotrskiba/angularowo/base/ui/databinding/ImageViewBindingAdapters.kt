package pl.piotrskiba.angularowo.base.ui.databinding

import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import pl.piotrskiba.angularowo.IntegerVersionSignature
import pl.piotrskiba.angularowo.utils.GlideUtils

@BindingAdapter("imageUrl", "placeholderImage", requireAll = false)
fun loadImage(
    imageView: ImageView,
    url: String?,
    @DrawableRes placeholder: Int?,
) {
    if (!url.isNullOrEmpty()) {
        Glide.with(imageView.context)
            .load(url)
            .signature(IntegerVersionSignature(GlideUtils.getSignatureVersionNumber(5)))
            .placeholder(placeholder ?: 0)
            .into(imageView)
    }
}

@BindingAdapter("android:src")
fun setImageResource(
    imageView: ImageView,
    @DrawableRes resource: Int,
) {
    imageView.setImageResource(resource)
}

@BindingAdapter("tint")
fun setTint(
    imageView: ImageView,
    @ColorRes tintColorRes: Int,
) {
    val color = ContextCompat.getColor(imageView.context, tintColorRes)
    imageView.setColorFilter(color)
}
