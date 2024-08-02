package pl.piotrskiba.angularowo.base

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.utils.NotificationUtils
import pl.piotrskiba.angularowo.utils.PreferenceUtils
import javax.inject.Inject

class AngularowoFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationUtils: NotificationUtils

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // TODO: drop PreferenceUtils
        val preferenceUtils = PreferenceUtils(applicationContext)
        if (preferenceUtils.uuid != null) {
            val notificationBody = remoteMessage.data[Constants.FIREBASE_FCM_DATA_NOTIFICATION_BODY]
            if (notificationBody != null) {
                val notificationTitle = remoteMessage.data[Constants.FIREBASE_FCM_DATA_NOTIFICATION_TITLE]
                val notificationSound = remoteMessage.data[Constants.FIREBASE_FCM_DATA_NOTIFICATION_SOUND]?.toBoolean()
                if (notificationTitle != null && notificationSound != null) {
                    notificationUtils.showNotification(notificationTitle, notificationBody, notificationSound)
                }
            }
        }
    }
}
