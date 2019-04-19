package pl.piotrskiba.angularowo.utils;

import android.net.Uri;

public class UrlUtils {

    private final static String BASE_CRAFATAR_URL = "https://crafatar.com/";

    private final static String BASE_AVATAR_PATH = "avatars/";
    private final static String BASE_BODY_PATH = "renders/body/";

    private final static String PARAM_SIZE = "size";
    private final static String PARAM_SHOW_OVERLAY = "overlay";

    private final static int DEFAULT_AVATAR_SIZE = 100;

    public static String buildAvatarUrl(String uuid, boolean showOverlay){
        Uri.Builder uriBuilder = Uri.parse(BASE_CRAFATAR_URL).buildUpon()
                .path(BASE_AVATAR_PATH + uuid)
                .appendQueryParameter(PARAM_SIZE, String.valueOf(DEFAULT_AVATAR_SIZE));

        if(showOverlay)
            uriBuilder.appendQueryParameter(PARAM_SHOW_OVERLAY, String.valueOf(true));

        return uriBuilder.build().toString();
    }

    public static String buildBodyUrl(String uuid, boolean showOverlay){
        Uri.Builder uriBuilder = Uri.parse(BASE_CRAFATAR_URL).buildUpon()
                .path(BASE_BODY_PATH + uuid);

        if(showOverlay)
            uriBuilder.appendQueryParameter(PARAM_SHOW_OVERLAY, String.valueOf(true));

        return uriBuilder.build().toString();
    }
}
