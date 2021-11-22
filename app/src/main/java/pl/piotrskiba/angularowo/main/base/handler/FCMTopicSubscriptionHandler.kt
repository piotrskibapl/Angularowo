package pl.piotrskiba.angularowo.main.base.handler

import com.google.firebase.messaging.FirebaseMessaging
import pl.piotrskiba.angularowo.BuildConfig
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import javax.inject.Inject

class FCMTopicSubscriptionHandler @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging,
    private val preferencesRepository: PreferencesRepository,
) {

    fun handleAppVersionTopicSubscription() {
        val subscribedVersion = preferencesRepository.subscribedFirebaseAppVersion
        val currentVersion = BuildConfig.VERSION_CODE
        if (subscribedVersion != currentVersion) {
            subscribedVersion?.let {
                firebaseMessaging.unsubscribeFromTopic(Constants.FIREBASE_APP_VERSION_TOPIC_PREFIX + it)
            }
            firebaseMessaging.subscribeToTopic(Constants.FIREBASE_APP_VERSION_TOPIC_PREFIX + currentVersion)
            preferencesRepository.subscribedFirebaseAppVersion = currentVersion
        }
    }

    fun handlePlayerUuidTopicSubscription() {
        val subscribedPlayerUuid = preferencesRepository.subscribedFirebasePlayerUuid
        val currentPlayerUuid = preferencesRepository.uuid!!
        if (subscribedPlayerUuid != currentPlayerUuid) {
            subscribedPlayerUuid?.let {
                firebaseMessaging.unsubscribeFromTopic(Constants.FIREBASE_PLAYER_UUID_TOPIC_PREFIX + it)
            }
            firebaseMessaging.subscribeToTopic(Constants.FIREBASE_PLAYER_UUID_TOPIC_PREFIX + currentPlayerUuid)
            preferencesRepository.subscribedFirebasePlayerUuid = currentPlayerUuid
        }
    }
}
