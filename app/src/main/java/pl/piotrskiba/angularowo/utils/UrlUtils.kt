package pl.piotrskiba.angularowo.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import pl.piotrskiba.angularowo.R

object UrlUtils {
    private const val BASE_CRAFATAR_URL = "https://crafatar.com/"

    private const val BASE_AVATAR_PATH = "avatars/"

    private const val PARAM_SIZE = "size"
    private const val PARAM_SHOW_OVERLAY = "overlay"

    private const val DEFAULT_AVATAR_SIZE = 100

    @JvmStatic
    fun buildAvatarUrl(uuid: String?, showOverlay: Boolean, context: Context): String {
        return when (uuid == null) {
            true -> Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(context.resources.getResourcePackageName(R.drawable.default_avatar))
                .appendPath(context.resources.getResourceTypeName(R.drawable.default_avatar))
                .appendPath(context.resources.getResourceEntryName(R.drawable.default_avatar))
                .build()
                .toString()

            false -> {
                val uriBuilder = Uri.parse(BASE_CRAFATAR_URL).buildUpon()
                    .path(BASE_AVATAR_PATH + uuid)
                    .appendQueryParameter(PARAM_SIZE, DEFAULT_AVATAR_SIZE.toString())
                if (showOverlay) {
                    uriBuilder.appendQueryParameter(PARAM_SHOW_OVERLAY, true.toString())
                }
                uriBuilder.build().toString()
            }
        }
    }
}
