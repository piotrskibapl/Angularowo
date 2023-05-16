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

    // TODO: preferences are not updated after subscription
    fun execute(appVersionCode: Int) =
        Completable.mergeArray(
            updateAppVersionTopic(appVersionCode),
            updatePlayerRankTopic(),
            updateNewEventsTopic(),
            updatePrivateMessagesTopic(),
            updateAccountIncidentsTopic(),
            updateNewReportsTopic(),
        )

    private fun updateAppVersionTopic(newVersion: Int): Completable =
        preferencesRepository.subscribedFirebaseAppVersion()
            .switchIfEmpty(cloudMessagingRepository.subscribeToAppVersion(newVersion).toSingleDefault(newVersion))
            .flatMapCompletable { subscribedVersion ->
                if (subscribedVersion != newVersion) {
                    cloudMessagingRepository.unsubscribeFromAppVersion(subscribedVersion)
                        .mergeWith(cloudMessagingRepository.subscribeToAppVersion(newVersion))
                } else {
                    Completable.complete()
                }
            }

    private fun updatePlayerRankTopic() =
        preferencesRepository.rankName()
            .toSingle()
            .flatMapCompletable { newRankName ->
                preferencesRepository.subscribedFirebasePlayerRankName()
                    .switchIfEmpty(cloudMessagingRepository.subscribeToPlayerRank(newRankName).toSingleDefault(newRankName))
                    .flatMapCompletable { subscribedRankName ->
                        if (subscribedRankName != newRankName) {
                            cloudMessagingRepository.unsubscribeFromPlayerRank(subscribedRankName)
                                .mergeWith(cloudMessagingRepository.subscribeToPlayerRank(newRankName))
                        } else {
                            Completable.complete()
                        }
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

    private fun updateNewReportsTopic() =
        preferencesRepository.rankName()
            .toSingle()
            .flatMapCompletable { rankName ->
                rankRepository.getAllRanks()
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
