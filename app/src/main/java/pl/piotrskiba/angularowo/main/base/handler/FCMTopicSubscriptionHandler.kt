package pl.piotrskiba.angularowo.main.base.handler

import com.google.firebase.messaging.FirebaseMessaging
import pl.piotrskiba.angularowo.BuildConfig
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.main.base.handler.FCMTopicSubscriptionAction.RESET
import pl.piotrskiba.angularowo.main.base.handler.FCMTopicSubscriptionAction.SUBSCRIBE
import pl.piotrskiba.angularowo.main.base.handler.FCMTopicSubscriptionAction.UNSUBSCRIBE
import pl.piotrskiba.angularowo.main.base.handler.FCMTopicSubscriptionAction.UPDATE_SUBSCRIPTION
import pl.piotrskiba.angularowo.utils.RankUtils
import javax.inject.Inject

class FCMTopicSubscriptionHandler @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging,
    private val preferencesRepository: PreferencesRepository,
) {

    fun handleAppVersionTopicSubscription(action: FCMTopicSubscriptionAction) {
        when (action) {
            UPDATE_SUBSCRIPTION -> updateAppVersionTopicSubscription()
            SUBSCRIBE -> updateAppVersionTopicSubscription()
            UNSUBSCRIBE -> unsubscribeAppVersionTopic()
            RESET -> unsubscribeAppVersionTopic()
        }
    }

    fun handlePlayerUuidTopicSubscription(action: FCMTopicSubscriptionAction) {
        when (action) {
            UPDATE_SUBSCRIPTION -> updatePlayerUuidTopicSubscription()
            SUBSCRIBE -> updatePlayerUuidTopicSubscription()
            UNSUBSCRIBE -> unsubscribePlayerUuidTopic()
            RESET -> unsubscribePlayerUuidTopic()
        }
    }

    fun handlePlayerRankTopicSubscription(action: FCMTopicSubscriptionAction) {
        when (action) {
            UPDATE_SUBSCRIPTION -> updatePlayerRankTopicSubscription()
            SUBSCRIBE -> updatePlayerRankTopicSubscription()
            UNSUBSCRIBE -> unsubscribePlayerRankTopic()
            RESET -> unsubscribePlayerRankTopic()
        }
    }

    fun handleNewEventsTopicSubscription(action: FCMTopicSubscriptionAction) {
        when (action) {
            UPDATE_SUBSCRIPTION -> updateNewEventsTopicSubscription()
            SUBSCRIBE -> subscribeNewEventsTopic()
            UNSUBSCRIBE -> unsubscribeNewEventsTopic(forceReset = false)
            RESET -> unsubscribeNewEventsTopic(forceReset = true)
        }
    }

    fun handlePrivateMessagesTopicSubscription(action: FCMTopicSubscriptionAction) {
        when (action) {
            UPDATE_SUBSCRIPTION -> updatePrivateMessagesTopicSubscription()
            SUBSCRIBE -> subscribePrivateMessagesTopic()
            UNSUBSCRIBE -> unsubscribePrivateMessagesTopic(forceReset = false)
            RESET -> unsubscribePrivateMessagesTopic(forceReset = true)
        }
    }

    fun handleAccountIncidentsTopicSubscription(action: FCMTopicSubscriptionAction) {
        when (action) {
            UPDATE_SUBSCRIPTION -> updateAccountIncidentsTopicSubscription()
            SUBSCRIBE -> subscribeAccountIncidentsTopic()
            UNSUBSCRIBE -> unsubscribeAccountIncidentsTopic(forceReset = false)
            RESET -> unsubscribeAccountIncidentsTopic(forceReset = true)
        }
    }

    fun handleNewReportsTopicSubscription(action: FCMTopicSubscriptionAction) {
        when (action) {
            UPDATE_SUBSCRIPTION -> updateNewReportsTopicSubscription()
            SUBSCRIBE -> subscribeNewReportsTopic()
            UNSUBSCRIBE -> unsubscribeNewReportsTopic(forceReset = false)
            RESET -> unsubscribeNewReportsTopic(forceReset = true)
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
        if (isSubscriptionUnknown) subscribeNewEventsTopic()
    }

    private fun subscribeNewEventsTopic() {
        val subscribed = preferencesRepository.subscribedToFirebaseEventsTopic == true
        if (!subscribed) {
            firebaseMessaging.subscribeToTopic(Constants.FIREBASE_NEW_EVENTS_TOPIC)
            preferencesRepository.subscribedToFirebaseEventsTopic = true
        }
    }

    private fun unsubscribeNewEventsTopic(forceReset: Boolean) {
        val subscribed = preferencesRepository.subscribedToFirebaseEventsTopic == true
        if (subscribed) {
            firebaseMessaging.unsubscribeFromTopic(Constants.FIREBASE_NEW_EVENTS_TOPIC)
            preferencesRepository.subscribedToFirebaseEventsTopic = if (forceReset) null else false
        }
    }

    private fun updatePrivateMessagesTopicSubscription() {
        val isSubscriptionUnknown = preferencesRepository.subscribedToFirebasePrivateMessagesTopic == null
        if (isSubscriptionUnknown) subscribePrivateMessagesTopic()
    }

    private fun subscribePrivateMessagesTopic() {
        val subscribed = preferencesRepository.subscribedToFirebasePrivateMessagesTopic == true
        if (!subscribed) {
            firebaseMessaging.subscribeToTopic(Constants.FIREBASE_PRIVATE_MESSAGES_TOPIC)
            preferencesRepository.subscribedToFirebasePrivateMessagesTopic = true
        }
    }

    private fun unsubscribePrivateMessagesTopic(forceReset: Boolean) {
        val subscribed = preferencesRepository.subscribedToFirebasePrivateMessagesTopic == true
        if (subscribed) {
            firebaseMessaging.unsubscribeFromTopic(Constants.FIREBASE_PRIVATE_MESSAGES_TOPIC)
            preferencesRepository.subscribedToFirebasePrivateMessagesTopic = if (forceReset) null else false
        }
    }

    private fun updateAccountIncidentsTopicSubscription() {
        val isSubscriptionUnknown = preferencesRepository.subscribedToFirebaseAccountIncidentsTopic == null
        if (isSubscriptionUnknown) subscribeAccountIncidentsTopic()
    }

    private fun subscribeAccountIncidentsTopic() {
        val subscribed = preferencesRepository.subscribedToFirebaseAccountIncidentsTopic == true
        if (!subscribed) {
            firebaseMessaging.subscribeToTopic(Constants.FIREBASE_ACCOUNT_INCIDENTS_TOPIC)
            preferencesRepository.subscribedToFirebaseAccountIncidentsTopic = true
        }
    }

    private fun unsubscribeAccountIncidentsTopic(forceReset: Boolean) {
        val subscribed = preferencesRepository.subscribedToFirebaseAccountIncidentsTopic == true
        if (subscribed) {
            firebaseMessaging.unsubscribeFromTopic(Constants.FIREBASE_ACCOUNT_INCIDENTS_TOPIC)
            preferencesRepository.subscribedToFirebaseAccountIncidentsTopic = if (forceReset) null else false
        }
    }

    private fun updateNewReportsTopicSubscription() {
        val isStaffMember = RankUtils.getRankFromName(preferencesRepository.rankName!!).staff
        val subscribed = preferencesRepository.subscribedToFirebaseNewReportsTopic
        when {
            subscribed == null && isStaffMember -> subscribeNewReportsTopic()
            subscribed == true && !isStaffMember -> unsubscribeNewReportsTopic(true)
        }
    }

    private fun subscribeNewReportsTopic() {
        val subscribed = preferencesRepository.subscribedToFirebaseNewReportsTopic == true
        if (!subscribed) {
            firebaseMessaging.subscribeToTopic(Constants.FIREBASE_NEW_REPORTS_TOPIC)
            preferencesRepository.subscribedToFirebaseNewReportsTopic = true
        }
    }

    private fun unsubscribeNewReportsTopic(forceReset: Boolean) {
        val subscribed = preferencesRepository.subscribedToFirebaseNewReportsTopic == true
        if (subscribed) {
            firebaseMessaging.unsubscribeFromTopic(Constants.FIREBASE_ACCOUNT_INCIDENTS_TOPIC)
            preferencesRepository.subscribedToFirebaseNewReportsTopic = if (forceReset) null else false
        }
    }
}
