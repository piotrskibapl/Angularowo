package pl.piotrskiba.angularowo.data.cloudmessaging.repository

import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.rxjava3.core.Completable
import pl.piotrskiba.angularowo.domain.cloudmessaging.repository.CloudMessagingRepository

private const val APP_VERSION_TOPIC_PREFIX = "version_"
private const val PLAYER_UUID_TOPIC_PREFIX = "player_"
private const val RANK_TOPIC_PREFIX = "rank_"
private const val NEW_EVENTS_TOPIC = "new_event"
private const val PRIVATE_MESSAGES_TOPIC = "private_messages"
private const val ACCOUNT_INCIDENTS_TOPIC = "account_incidents"
private const val NEW_REPORTS_TOPIC = "new_reports"

class CloudMessagingRepositoryImpl(
    private val firebaseMessaging: FirebaseMessaging,
) : CloudMessagingRepository {

    override fun subscribeToAppVersion(versionCode: Int): Completable =
        Completable.fromAction {
            firebaseMessaging.subscribeToTopic(APP_VERSION_TOPIC_PREFIX + versionCode)
        }

    override fun unsubscribeFromAppVersion(versionCode: Int): Completable =
        Completable.fromAction {
            firebaseMessaging.unsubscribeFromTopic(APP_VERSION_TOPIC_PREFIX + versionCode)
        }

    override fun subscribeToPlayerUuid(uuid: String): Completable =
        Completable.fromAction {
            firebaseMessaging.subscribeToTopic(PLAYER_UUID_TOPIC_PREFIX + uuid)
        }

    override fun unsubscribeFromPlayerUuid(uuid: String): Completable =
        Completable.fromAction {
            firebaseMessaging.unsubscribeFromTopic(PLAYER_UUID_TOPIC_PREFIX + uuid)
        }

    override fun subscribeToPlayerRank(rankName: String): Completable =
        Completable.fromAction {
            firebaseMessaging.subscribeToTopic(RANK_TOPIC_PREFIX + rankName)
        }

    override fun unsubscribeFromPlayerRank(rankName: String): Completable =
        Completable.fromAction {
            firebaseMessaging.unsubscribeFromTopic(RANK_TOPIC_PREFIX + rankName)
        }

    override fun subscribeToNewEvents(): Completable =
        Completable.fromAction {
            firebaseMessaging.subscribeToTopic(NEW_EVENTS_TOPIC)
        }

    override fun unsubscribeFromNewEvents(): Completable =
        Completable.fromAction {
            firebaseMessaging.unsubscribeFromTopic(NEW_EVENTS_TOPIC)
        }

    override fun subscribeToPrivateMessages(): Completable =
        Completable.fromAction {
            firebaseMessaging.subscribeToTopic(PRIVATE_MESSAGES_TOPIC)
        }

    override fun unsubscribeFromPrivateMessages(): Completable =
        Completable.fromAction {
            firebaseMessaging.unsubscribeFromTopic(PRIVATE_MESSAGES_TOPIC)
        }

    override fun subscribeToAccountIncidents(): Completable =
        Completable.fromAction {
            firebaseMessaging.subscribeToTopic(ACCOUNT_INCIDENTS_TOPIC)
        }

    override fun unsubscribeFromAccountIncidents(): Completable =
        Completable.fromAction {
            firebaseMessaging.unsubscribeFromTopic(ACCOUNT_INCIDENTS_TOPIC)
        }

    override fun subscribeToNewReports(): Completable =
        Completable.fromAction {
            firebaseMessaging.subscribeToTopic(NEW_REPORTS_TOPIC)
        }

    override fun unsubscribeFromNewReports(): Completable =
        Completable.fromAction {
            firebaseMessaging.unsubscribeFromTopic(NEW_REPORTS_TOPIC)
        }
}
