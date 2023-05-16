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

    private fun updateAppVersionTopic(newVersion: Int): Completable =
        preferencesRepository.subscribedFirebaseAppVersion()
            .switchIfEmpty(subscribeToAppVersion(newVersion).toSingleDefault(newVersion))
            .flatMapCompletable { subscribedVersion ->
                if (subscribedVersion != newVersion) {
                    cloudMessagingRepository.unsubscribeFromAppVersion(subscribedVersion)
                        .andThen(subscribeToAppVersion(newVersion))
                } else {
                    Completable.complete()
                }
            }

    private fun subscribeToAppVersion(version: Int) =
        cloudMessagingRepository.subscribeToAppVersion(version)
            .andThen(preferencesRepository.setSubscribedFirebaseAppVersion(version))

    private fun updatePlayerRankTopic() =
        preferencesRepository.rankName()
            .toSingle()
            .flatMapCompletable { newRankName ->
                preferencesRepository.subscribedFirebasePlayerRankName()
                    .switchIfEmpty(subscribeToPlayerRank(newRankName).toSingleDefault(newRankName))
                    .flatMapCompletable { subscribedRankName ->
                        if (subscribedRankName != newRankName) {
                            cloudMessagingRepository.unsubscribeFromPlayerRank(subscribedRankName)
                                .andThen(subscribeToPlayerRank(newRankName))
                        } else {
                            Completable.complete()
                        }
                    }
            }

    private fun subscribeToPlayerRank(rankName: String) =
        cloudMessagingRepository.subscribeToPlayerRank(rankName)
            .andThen(preferencesRepository.setSubscribedFirebasePlayerRankName(rankName))

    private fun updateNewEventsTopic() =
        preferencesRepository.subscribedToFirebaseEventsTopic()
            .switchIfEmpty(
                cloudMessagingRepository.subscribeToNewEvents()
                    .andThen(preferencesRepository.setSubscribedToFirebaseEventsTopic(true))
                    .toSingleDefault(true)
            )
            .ignoreElement()

    private fun updatePrivateMessagesTopic() =
        preferencesRepository.subscribedToFirebasePrivateMessagesTopic()
            .switchIfEmpty(
                cloudMessagingRepository.subscribeToPrivateMessages()
                    .andThen(preferencesRepository.setSubscribedToFirebasePrivateMessagesTopic(true))
                    .toSingleDefault(true)
            )
            .ignoreElement()

    private fun updateAccountIncidentsTopic() =
        preferencesRepository.subscribedToFirebaseAccountIncidentsTopic()
            .switchIfEmpty(
                cloudMessagingRepository.subscribeToAccountIncidents()
                    .andThen(preferencesRepository.setSubscribedToFirebaseAccountIncidentsTopic(true))
                    .toSingleDefault(true)
            )
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
                                        .andThen(preferencesRepository.setSubscribedToFirebaseNewReportsTopic(false))
                                } else {
                                    Completable.complete()
                                }
                            }
                    }
            }

    private fun handleFirstTimeNewReportsSubscription(isStaff: Boolean) =
        if (isStaff) {
            cloudMessagingRepository.subscribeToNewReports()
                .andThen(preferencesRepository.setSubscribedToFirebaseNewReportsTopic(true))
        } else {
            Completable.complete()
        }.toSingleDefault(isStaff)
}
