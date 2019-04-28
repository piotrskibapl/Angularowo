package pl.piotrskiba.angularowo;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import pl.piotrskiba.angularowo.utils.NotificationUtils;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if(remoteMessage.getData() != null){
            String notification_title = remoteMessage.getData().get(Constants.FIREBASE_FCM_DATA_NOTIFICATION_TITLE);
            String notification_body = remoteMessage.getData().get(Constants.FIREBASE_FCM_DATA_NOTIFICATION_BODY);
            boolean notification_sound = Boolean.parseBoolean(
                    remoteMessage.getData().get(Constants.FIREBASE_FCM_DATA_NOTIFICATION_SOUND)
            );

            if(notification_body != null)
                new NotificationUtils(getApplicationContext())
                        .showNotification(notification_title, notification_body, notification_sound);
        }
    }
}
