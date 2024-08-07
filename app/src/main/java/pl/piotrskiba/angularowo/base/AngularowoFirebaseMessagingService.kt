package pl.piotrskiba.angularowo.base

import android.content.SharedPreferences
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.utils.NotificationUtils
import javax.inject.Inject

// TODO: should be moved to data package
class AngularowoFirebaseMessagingService : FirebaseMessagingService() {

    private object FcmNotificationData {
        const val TITLE = "notification_title"
        const val BODY = "notification_body"
        const val SOUND = "notification_sound"
    }

    @Inject
    lateinit var notificationUtils: NotificationUtils

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val uuid = sharedPreferences.getString(applicationContext.getString(R.string.pref_key_uuid), null)
        val notificationBody = remoteMessage.data[FcmNotificationData.BODY]
        val notificationTitle = remoteMessage.data[FcmNotificationData.TITLE]
        val notificationSound = remoteMessage.data[FcmNotificationData.SOUND]?.toBoolean()
        if (uuid != null && notificationTitle != null && notificationBody != null && notificationSound != null) {
            notificationUtils.showNotification(notificationTitle, notificationBody, notificationSound)
        }
    }
}
