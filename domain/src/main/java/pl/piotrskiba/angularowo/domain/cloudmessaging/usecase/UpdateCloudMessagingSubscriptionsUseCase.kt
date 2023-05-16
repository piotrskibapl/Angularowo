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
        preferencesRepository.subscribedToFirebaseEventsTopic()
            .switchIfEmpty(cloudMessagingRepository.subscribeToNewEvents().toSingleDefault(true))
            .ignoreElement()

    private fun updatePrivateMessagesTopic() =
        preferencesRepository.subscribedToFirebasePrivateMessagesTopic()
            .switchIfEmpty(cloudMessagingRepository.subscribeToPrivateMessages().toSingleDefault(true))
            .ignoreElement()

    private fun updateAccountIncidentsTopic() =
        preferencesRepository.subscribedToFirebaseAccountIncidentsTopic()
            .switchIfEmpty(cloudMessagingRepository.subscribeToAccountIncidents().toSingleDefault(true))
            .ignoreElement()

    private fun updateNewReportsTopic() =
        preferencesRepository.rankName()
            .toSingle()
            .flatMapCompletable { rankName ->
                rankRepository.getAllRanks()
                    .flatMapCompletable { ranks ->
                        val isStaff = ranks.firstOrNull { it.name == rankName }?.staff ?: false
                        preferencesRepository.subscribedToFirebaseNewReportsTopic()
                            .switchIfEmpty(handleFirstTimeNewReportsSubscription(isStaff))
                            .flatMapCompletable { subscribed ->
                                if (subscribed && !isStaff) {
                                    cloudMessagingRepository.unsubscribeFromNewReports()
                                } else {
                                    Completable.complete()
                                }
                            }
                    }
            }

    private fun handleFirstTimeNewReportsSubscription(isStaff: Boolean) =
        if (isStaff) {
            cloudMessagingRepository.subscribeToNewReports()
        } else {
            Completable.complete()
        }.toSingleDefault(isStaff)
}
