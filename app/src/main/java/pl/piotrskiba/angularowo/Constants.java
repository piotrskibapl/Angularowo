package pl.piotrskiba.angularowo;

public class Constants {

    public final static String ADMOB_APP_ID = "ca-app-pub-7790074991647252~4144564614";

    public final static String EXTRA_PLAYER = "extra_player";
    public final static String EXTRA_BAN = "extra_ban";
    public final static String EXTRA_BITMAP = "extra_bitmap";

    public final static int REQUEST_CODE_REGISTER = 1100;

    public final static int RESULT_CODE_SUCCESS = 1200;

    public final static String FIREBASE_PLAYER_TOPIC_PREFIX = "player_";
    public final static String FIREBASE_RANK_TOPIC_PREFIX = "rank_";
    public final static String FIREBASE_NEW_REPORTS_TOPIC = "new_reports";
    public final static String FIREBASE_NEW_EVENT_TOPIC = "new_event";

    public final static String FIREBASE_FCM_DATA_NOTIFICATION_TITLE = "notification_title";
    public final static String FIREBASE_FCM_DATA_NOTIFICATION_BODY = "notification_body";
    public final static String FIREBASE_FCM_DATA_NOTIFICATION_SOUND = "notification_sound";

    public final static String BAN_TYPES = "ban,warning,mute";
    public final static String ACTIVE_BAN_TYPES = "ban,warning";

    public static final long[] DEFAULT_VIBRATE_PATTERN = {0, 250, 250, 250};

    public final static String DEFAULT_CHANNEL_ID = "default_notification_channel";

    public final static String CHAT_WEBSOCKET_URL = "ws://164.132.159.123:25772";
    public final static String CHAT_WEBSOCKET_ACCESSTOKEN_HEADER = "access_token";
}
