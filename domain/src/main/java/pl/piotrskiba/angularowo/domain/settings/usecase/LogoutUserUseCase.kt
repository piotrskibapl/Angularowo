package pl.piotrskiba.angularowo.domain.settings.usecase

import io.reactivex.rxjava3.core.Completable
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.cloudmessaging.repository.CloudMessagingRepository
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val cloudMessagingRepository: CloudMessagingRepository,
) {

    fun execute(appVersionCode: Int) =
        preferencesRepository.uuid()
            .zipWith(preferencesRepository.rankName(), ::Pair)
            .toSingle()
            .flatMapCompletable { (uuid, rankName) ->
                Completable.mergeArray(
                    cloudMessagingRepository.unsubscribeFromAppVersion(appVersionCode),
                    cloudMessagingRepository.unsubscribeFromPlayerUuid(uuid),
                    cloudMessagingRepository.unsubscribeFromPlayerRank(rankName),
                    cloudMessagingRepository.unsubscribeFromNewEvents(),
                    cloudMessagingRepository.unsubscribeFromPrivateMessages(),
                    cloudMessagingRepository.unsubscribeFromAccountIncidents(),
                    cloudMessagingRepository.unsubscribeFromNewReports(),
                ).also {
                    preferencesRepository.clearUserData()
                }
            }
}
