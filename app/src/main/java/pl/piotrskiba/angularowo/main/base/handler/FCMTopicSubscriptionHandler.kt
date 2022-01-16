package pl.piotrskiba.angularowo.main.base.handler

import com.google.firebase.messaging.FirebaseMessaging
import pl.piotrskiba.angularowo.BuildConfig
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.main.base.handler.FCMTopicSubscriptionAction.UNSUBSCRIBE
import pl.piotrskiba.angularowo.main.base.handler.FCMTopicSubscriptionAction.UPDATE_SUBSCRIPTION
import javax.inject.Inject

class FCMTopicSubscriptionHandler @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging,
    private val preferencesRepository: PreferencesRepository,
) {

    fun handleAppVersionTopicSubscription(action: FCMTopicSubscriptionAction) {
        when (action) {
            UPDATE_SUBSCRIPTION -> updateAppVersionTopicSubscription()
            UNSUBSCRIBE -> unsubscribeAppVersionTopic()
        }
    }

    fun handlePlayerUuidTopicSubscription(action: FCMTopicSubscriptionAction) {
        when (action) {
            UPDATE_SUBSCRIPTION -> updatePlayerUuidTopicSubscription()
            UNSUBSCRIBE -> unsubscribePlayerUuidTopic()
        }
    }

    fun handlePlayerRankTopicSubscription(action: FCMTopicSubscriptionAction) {
        when (action) {
            UPDATE_SUBSCRIPTION -> updatePlayerRankTopicSubscription()
            UNSUBSCRIBE -> unsubscribePlayerRankTopic()
        }
    }

    fun handleNewEventsTopicSubscription(action: FCMTopicSubscriptionAction) {
        when (action) {
            UPDATE_SUBSCRIPTION -> updateNewEventsTopicSubscription()
            UNSUBSCRIBE -> unsubscribeNewEventsTopic()
        }
    }

    fun handlePrivateMessagesTopicSubscription(action: FCMTopicSubscriptionAction) {
        when (action) {
            UPDATE_SUBSCRIPTION -> updatePrivateMessagesTopicSubscription()
            UNSUBSCRIBE -> unsubscribePrivateMessagesTopic()
        }
    }

    fun handleAccountIncidentsTopicSubscription(action: FCMTopicSubscriptionAction) {
        when (action) {
            UPDATE_SUBSCRIPTION -> updateAccountIncidentsTopicSubscription()
            UNSUBSCRIBE -> unsubscribeAccountIncidentsTopic()
        }
    }

    private fun updateAppVersionTopicSubscription() {
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

    private fun unsubscribeAppVersionTopic() {
        val subscribedVersion = preferencesRepository.subscribedFirebaseAppVersion
        subscribedVersion?.let {
            firebaseMessaging.unsubscribeFromTopic(Constants.FIREBASE_APP_VERSION_TOPIC_PREFIX + it)
            preferencesRepository.subscribedFirebaseAppVersion = null
        }
    }

    private fun updatePlayerUuidTopicSubscription() {
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

    private fun unsubscribePlayerUuidTopic() {
        val subscribedPlayerUuid = preferencesRepository.subscribedFirebasePlayerUuid
        subscribedPlayerUuid?.let {
            firebaseMessaging.unsubscribeFromTopic(Constants.FIREBASE_PLAYER_UUID_TOPIC_PREFIX + it)
        }
        preferencesRepository.subscribedFirebasePlayerUuid = null
    }

    private fun updatePlayerRankTopicSubscription() {
        val subscribedPlayerRankName = preferencesRepository.subscribedFirebasePlayerRankName
        val currentPlayerRankName = preferencesRepository.rankName!!
        if (subscribedPlayerRankName != currentPlayerRankName) {
            subscribedPlayerRankName?.let {
                firebaseMessaging.unsubscribeFromTopic(Constants.FIREBASE_RANK_TOPIC_PREFIX + it)
            }
            firebaseMessaging.subscribeToTopic(Constants.FIREBASE_RANK_TOPIC_PREFIX + currentPlayerRankName)
            preferencesRepository.subscribedFirebasePlayerRankName = currentPlayerRankName
        }
    }

    private fun unsubscribePlayerRankTopic() {
        val subscribedPlayerRankName = preferencesRepository.subscribedFirebasePlayerRankName
        subscribedPlayerRankName?.let {
            firebaseMessaging.unsubscribeFromTopic(Constants.FIREBASE_RANK_TOPIC_PREFIX + it)
        }
        preferencesRepository.subscribedFirebasePlayerRankName = null
    }

    private fun updateNewEventsTopicSubscription() {
        val isSubscriptionUnknown = preferencesRepository.subscribedToFirebaseEventsTopic == null
        if (isSubscriptionUnknown) {
            firebaseMessaging.subscribeToTopic(Constants.FIREBASE_NEW_EVENTS_TOPIC)
            preferencesRepository.subscribedToFirebaseEventsTopic = true
        }
    }

    private fun unsubscribeNewEventsTopic() {
        val isSubscriptionUnknown = preferencesRepository.subscribedToFirebaseEventsTopic == null
        if (!isSubscriptionUnknown) {
            firebaseMessaging.unsubscribeFromTopic(Constants.FIREBASE_NEW_EVENTS_TOPIC)
            preferencesRepository.subscribedToFirebaseEventsTopic = null
        }
    }

    private fun updatePrivateMessagesTopicSubscription() {
        val isSubscriptionUnknown = preferencesRepository.subscribedToFirebasePrivateMessagesTopic == null
        if (isSubscriptionUnknown) {
            firebaseMessaging.subscribeToTopic(Constants.FIREBASE_PRIVATE_MESSAGES_TOPIC)
            preferencesRepository.subscribedToFirebasePrivateMessagesTopic = true
        }
    }

    private fun unsubscribePrivateMessagesTopic() {
        val isSubscriptionUnknown = preferencesRepository.subscribedToFirebasePrivateMessagesTopic == null
        if (!isSubscriptionUnknown) {
            firebaseMessaging.unsubscribeFromTopic(Constants.FIREBASE_PRIVATE_MESSAGES_TOPIC)
            preferencesRepository.subscribedToFirebasePrivateMessagesTopic = null
        }
    }

    private fun updateAccountIncidentsTopicSubscription() {
        val isSubscriptionUnknown = preferencesRepository.subscribedToFirebaseAccountIncidentsTopic == null
        if (isSubscriptionUnknown) {
            firebaseMessaging.subscribeToTopic(Constants.FIREBASE_ACCOUNT_INCIDENTS_TOPIC)
            preferencesRepository.subscribedToFirebaseAccountIncidentsTopic = true
        }
    }

    private fun unsubscribeAccountIncidentsTopic() {
        val isSubscriptionUnknown = preferencesRepository.subscribedToFirebaseAccountIncidentsTopic == null
        if (!isSubscriptionUnknown) {
            firebaseMessaging.unsubscribeFromTopic(Constants.FIREBASE_ACCOUNT_INCIDENTS_TOPIC)
            preferencesRepository.subscribedToFirebaseAccountIncidentsTopic = null
        }
    }
}
