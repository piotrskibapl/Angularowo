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

    fun handleNewEventsTopicSubscription() {
        val isSubscriptionUnknown = preferencesRepository.subscribedToFirebaseEventsTopic == null
        if (isSubscriptionUnknown) {
            firebaseMessaging.subscribeToTopic(Constants.FIREBASE_NEW_EVENTS_TOPIC)
            preferencesRepository.subscribedToFirebaseEventsTopic = true
        }
    }

    fun handlePrivateMessagesTopicSubscription() {
        val isSubscriptionUnknown = preferencesRepository.subscribedToFirebasePrivateMessagesTopic == null
        if (isSubscriptionUnknown) {
            firebaseMessaging.subscribeToTopic(Constants.FIREBASE_PRIVATE_MESSAGES_TOPIC)
            preferencesRepository.subscribedToFirebasePrivateMessagesTopic = true
        }
    }

    fun handleAccountIncidentsTopicSubscription() {
        val isSubscriptionUnknown = preferencesRepository.subscribedToFirebaseAccountIncidentsTopic == null
        if (isSubscriptionUnknown) {
            firebaseMessaging.subscribeToTopic(Constants.FIREBASE_ACCOUNT_INCIDENTS_TOPIC)
            preferencesRepository.subscribedToFirebaseAccountIncidentsTopic = true
        }
    }
}
