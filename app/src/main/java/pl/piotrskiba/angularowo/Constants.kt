package pl.piotrskiba.angularowo

object Constants {
    const val EXTRA_PLAYER = "extra_player"
    const val EXTRA_PREVIEWED_PLAYER = "extra_previewed_player"
    const val EXTRA_PUNISHMENT = "extra_punishment"
    const val EXTRA_REPORT = "extra_report"

    const val REQUEST_CODE_REGISTER = 1100
    const val RESULT_CODE_SUCCESS = 1200

    const val FIREBASE_FCM_DATA_NOTIFICATION_TITLE = "notification_title"
    const val FIREBASE_FCM_DATA_NOTIFICATION_BODY = "notification_body"
    const val FIREBASE_FCM_DATA_NOTIFICATION_SOUND = "notification_sound"

    val DEFAULT_VIBRATE_PATTERN = longArrayOf(0, 250, 250, 250)

    const val DEFAULT_CHANNEL_ID = "default_notification_channel"
    const val SILENT_CHANNEL_ID = "silent_notification_channel"

    const val USERNAME_QUALIFIER = "%player%"

    const val REMOTE_CONFIG_RANKS_KEY = "ranks"
    const val REMOTE_CONFIG_APP_LOCK_START_TIMESTAMP = "app_lock_start_timestamp"
    const val REMOTE_CONFIG_APP_LOCK_END_TIMESTAMP = "app_lock_end_timestamp"
}

object Permissions {
    const val IGNORE_APP_LOCK = "ignore_app_lock"
    const val MUTE_PLAYERS = "mute_players"
    const val KICK_PLAYERS = "kick_players"
    const val WARN_PLAYERS = "warn_players"
    const val BAN_PLAYERS = "ban_players"
}
