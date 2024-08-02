package pl.piotrskiba.angularowo.base.ui.databinding

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.IntegerVersionSignature
import java.util.Calendar

private const val CRAFATAR_URL = "https://crafatar.com/"
private const val CRAFATAR_BODY_PATH = "renders/body/"
private const val CRAFATAR_SHOW_OVERLAY_PARAMETER = "overlay"
private const val IMAGE_LIFETIME_DAYS = 5

@BindingAdapter("imageUrl", "placeholderImage", requireAll = false)
fun loadImage(
    imageView: ImageView,
    url: String?,
    @DrawableRes placeholder: Int?,
) {
    if (!url.isNullOrEmpty()) {
        Glide.with(imageView.context)
            .load(url)
            .signature(
                IntegerVersionSignature(
                    with(Calendar.getInstance()) {
                        get(Calendar.YEAR) * 1000 + get(Calendar.DAY_OF_YEAR) / IMAGE_LIFETIME_DAYS
                    },
                ),
            )
            .placeholder(placeholder ?: 0)
            .into(imageView)
    }
}

@BindingAdapter("crafatarBodyUuid")
fun loadCrafatarBody(
    imageView: ImageView,
    uuid: String?,
) {
    if (uuid != null) {
        val url = Uri.parse(CRAFATAR_URL)
            .buildUpon()
            .path(CRAFATAR_BODY_PATH + uuid)
            .appendQueryParameter(CRAFATAR_SHOW_OVERLAY_PARAMETER, true.toString())
            .build()
            .toString()
        Glide.with(imageView.context)
            .load(url)
            .signature(
                IntegerVersionSignature(
                    with(Calendar.getInstance()) {
                        get(Calendar.YEAR) * 1000 + get(Calendar.DAY_OF_YEAR) / IMAGE_LIFETIME_DAYS
                    },
                ),
            )
            .placeholder(R.drawable.default_body)
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
