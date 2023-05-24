package pl.piotrskiba.angularowo

object Constants {
    const val REQUEST_CODE_REGISTER = 1100
    const val RESULT_CODE_SUCCESS = 1200

    const val FIREBASE_FCM_DATA_NOTIFICATION_TITLE = "notification_title"
    const val FIREBASE_FCM_DATA_NOTIFICATION_BODY = "notification_body"
    const val FIREBASE_FCM_DATA_NOTIFICATION_SOUND = "notification_sound"

    val DEFAULT_VIBRATE_PATTERN = longArrayOf(0, 250, 250, 250)

    const val DEFAULT_CHANNEL_ID = "default_notification_channel"
    const val SILENT_CHANNEL_ID = "silent_notification_channel"

    const val USERNAME_QUALIFIER = "%player%"
}
