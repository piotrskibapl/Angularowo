package pl.piotrskiba.angularowo.domain.settings.usecase

import pl.piotrskiba.angularowo.domain.cloudmessaging.repository.CloudMessagingRepository
import javax.inject.Inject

class UpdateAccountIncidentsSubscriptionUseCase @Inject constructor(
    private val cloudMessagingRepository: CloudMessagingRepository,
) {

    fun execute(value: Boolean) =
        if (value) {
            cloudMessagingRepository.subscribeToAccountIncidents()
        } else {
            cloudMessagingRepository.unsubscribeFromAccountIncidents()
        }
}
