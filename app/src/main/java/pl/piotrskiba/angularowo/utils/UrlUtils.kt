package pl.piotrskiba.angularowo.utils

import android.net.Uri

object UrlUtils {
    private const val BASE_CRAFATAR_URL = "https://crafatar.com/"

    private const val BASE_AVATAR_PATH = "avatars/"
    private const val BASE_BODY_PATH = "renders/body/"

    private const val PARAM_SIZE = "size"
    private const val PARAM_SHOW_OVERLAY = "overlay"

    private const val DEFAULT_AVATAR_SIZE = 100

    @JvmStatic
    fun buildAvatarUrl(uuid: String, showOverlay: Boolean): String {
        val uriBuilder = Uri.parse(BASE_CRAFATAR_URL).buildUpon()
                .path(BASE_AVATAR_PATH + uuid)
                .appendQueryParameter(PARAM_SIZE, DEFAULT_AVATAR_SIZE.toString())

        if (showOverlay)
            uriBuilder.appendQueryParameter(PARAM_SHOW_OVERLAY, true.toString())

        return uriBuilder.build().toString()
    }

    @JvmStatic
    fun buildBodyUrl(uuid: String, showOverlay: Boolean): String {
        val uriBuilder = Uri.parse(BASE_CRAFATAR_URL).buildUpon()
                .path(BASE_BODY_PATH + uuid)

        if (showOverlay)
            uriBuilder.appendQueryParameter(PARAM_SHOW_OVERLAY, true.toString())

        return uriBuilder.build().toString()
    }
}