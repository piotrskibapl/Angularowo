package pl.piotrskiba.angularowo

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import pl.piotrskiba.angularowo.utils.NotificationUtils
import pl.piotrskiba.angularowo.utils.PreferenceUtils

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val preferenceUtils = PreferenceUtils(applicationContext)

        // show notifications to logged in users only
        if (preferenceUtils.uuid != null) {
            val notificationBody = remoteMessage.data[Constants.FIREBASE_FCM_DATA_NOTIFICATION_BODY]

            notificationBody?.run {
                val notificationTitle = remoteMessage.data[Constants.FIREBASE_FCM_DATA_NOTIFICATION_TITLE]
                val notificationSound = remoteMessage.data[Constants.FIREBASE_FCM_DATA_NOTIFICATION_SOUND]?.toBoolean()

                if (notificationTitle != null && notificationSound != null) {
                    NotificationUtils(applicationContext)
                            .showNotification(notificationTitle, notificationBody, notificationSound)
                }
            }
        }
    }
}