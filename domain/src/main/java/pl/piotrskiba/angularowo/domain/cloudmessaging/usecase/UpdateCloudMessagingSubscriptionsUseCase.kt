package pl.piotrskiba.angularowo.domain.cloudmessaging.usecase

import io.reactivex.rxjava3.core.Completable
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.cloudmessaging.repository.CloudMessagingRepository
import pl.piotrskiba.angularowo.domain.rank.repository.RankRepository
import javax.inject.Inject

class UpdateCloudMessagingSubscriptionsUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val cloudMessagingRepository: CloudMessagingRepository,
    private val rankRepository: RankRepository,
) {

    fun execute(appVersionCode: Int) =
        Completable.mergeArray(
            updateAppVersionTopic(appVersionCode),
            updatePlayerRankTopic(),
            updateNewEventsTopic(),
            updatePrivateMessagesTopic(),
            updateAccountIncidentsTopic(),
            updateNewReportsTopic(),
        )

    private fun updateAppVersionTopic(current: Int): Completable {
        val subscribed = preferencesRepository.subscribedFirebaseAppVersion
        return if (subscribed != current) {
            Completable.create { emitter ->
                if (subscribed != null) {
                    cloudMessagingRepository.unsubscribeFromAppVersion(subscribed)
                        .doOnComplete { emitter.onComplete() }
                } else {
                    emitter.onComplete()
                }
            }.concatWith(cloudMessagingRepository.subscribeToAppVersion(current))
        } else {
            Completable.complete()
        }
    }

    private fun updatePlayerRankTopic(): Completable {
        val subscribed = preferencesRepository.subscribedFirebasePlayerRankName
        val current = preferencesRepository.rankName!!
        return if (subscribed != current) {
            Completable.create { emitter ->
                if (subscribed != null) {
                    cloudMessagingRepository.unsubscribeFromPlayerRank(subscribed)
                        .doOnComplete { emitter.onComplete() }
                } else {
                    emitter.onComplete()
                }
            }.concatWith(cloudMessagingRepository.subscribeToPlayerRank(current))
        } else {
            Completable.complete()
        }
    }

    private fun updateNewEventsTopic() =
        if (preferencesRepository.subscribedToFirebaseEventsTopic == null) {
            cloudMessagingRepository.subscribeToNewEvents()
        } else {
            Completable.complete()
        }

    private fun updatePrivateMessagesTopic() =
        if (preferencesRepository.subscribedToFirebasePrivateMessagesTopic == null) {
            cloudMessagingRepository.subscribeToPrivateMessages()
        } else {
            Completable.complete()
        }

    private fun updateAccountIncidentsTopic() =
        if (preferencesRepository.subscribedToFirebaseAccountIncidentsTopic == null) {
            cloudMessagingRepository.subscribeToAccountIncidents()
        } else {
            Completable.complete()
        }

    private fun updateNewReportsTopic(): Completable {
        val rankName = preferencesRepository.rankName!!
        return rankRepository.getAllRanks()
            .flatMapCompletable { ranks ->
                val isStaff = ranks.firstOrNull { it.name == rankName }?.staff ?: false
                val subscribed = preferencesRepository.subscribedToFirebaseNewReportsTopic
                when {
                    subscribed == null && isStaff -> cloudMessagingRepository.subscribeToNewReports()
                    subscribed == true && !isStaff -> cloudMessagingRepository.unsubscribeFromNewReports()
                    else -> Completable.complete()
                }
            }
    }
}
