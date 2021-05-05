package pl.piotrskiba.angularowo.base.ui.databinding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import pl.piotrskiba.angularowo.IntegerVersionSignature
import pl.piotrskiba.angularowo.utils.GlideUtils

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(url)
            .signature(IntegerVersionSignature(GlideUtils.getSignatureVersionNumber(5)))
            .into(view)
    }
}
