package pl.piotrskiba.angularowo.base

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.utils.NotificationUtils
import pl.piotrskiba.angularowo.utils.PreferenceUtils

class AngularowoFirebaseMessagingService : FirebaseMessagingService() {

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
                    NotificationUtils(applicationContext).showNotification(notificationTitle, notificationBody, notificationSound)
                }
            }
        }
    }
}
