package pl.piotrskiba.angularowo

object Constants {
    const val ADMOB_APP_ID = "ca-app-pub-7790074991647252~4144564614"

    const val EXTRA_PLAYER = "extra_player"
    const val EXTRA_BAN = "extra_ban"
    const val EXTRA_BITMAP = "extra_bitmap"
    const val EXTRA_REPORT = "extra_report"

    const val REQUEST_CODE_REGISTER = 1100
    const val RESULT_CODE_SUCCESS = 1200

    const val FIREBASE_APP_VERSION_TOPIC_PREFIX = "version_"
    const val FIREBASE_PLAYER_TOPIC_PREFIX = "player_"
    const val FIREBASE_RANK_TOPIC_PREFIX = "rank_"
    const val FIREBASE_NEW_REPORTS_TOPIC = "new_reports"
    const val FIREBASE_NEW_EVENT_TOPIC = "new_event"

    const val FIREBASE_FCM_DATA_NOTIFICATION_TITLE = "notification_title"
    const val FIREBASE_FCM_DATA_NOTIFICATION_BODY = "notification_body"
    const val FIREBASE_FCM_DATA_NOTIFICATION_SOUND = "notification_sound"

    const val BAN_TYPES = "ban,warning,mute,kick"
    const val ACTIVE_BAN_TYPES = "ban,warning,mute"

    val DEFAULT_VIBRATE_PATTERN = longArrayOf(0, 250, 250, 250)

    const val DEFAULT_CHANNEL_ID = "default_notification_channel"
    const val SILENT_CHANNEL_ID = "silent_notification_channel"

    const val NOTIFICATION_USERNAME_QUALIFIER = "%player%"

    const val CHAT_WEBSOCKET_URL = "ws://asmc-serwer.piotrskiba.pl:25772"
    const val CHAT_WEBSOCKET_ACCESSTOKEN_HEADER = "access_token"

    const val API_RESPONSE_REPORT_ACCEPTED = "True"
    const val API_RESPONSE_REPORT_REJECTED = "False"
    const val API_RESPONSE_REPORT_PENDING = "None"
    const val API_RESPONSE_REPORT_UNCERTAIN = "Uncertain"

    const val REMOTE_CONFIG_RANKS_KEY = "ranks"
    const val REMOTE_CONFIG_APP_LOCK_TITLE_KEY = "app_lock_title"
    const val REMOTE_CONFIG_APP_LOCK_BODY_KEY = "app_lock_body"
    const val REMOTE_CONFIG_APP_LOCK_START_TIMESTAMP = "app_lock_start_timestamp"
    const val REMOTE_CONFIG_APP_LOCK_END_TIMESTAMP = "app_lock_end_timestamp"
}