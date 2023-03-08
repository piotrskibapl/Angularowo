package pl.piotrskiba.angularowo.domain.settings.usecase

import pl.piotrskiba.angularowo.domain.cloudmessaging.repository.CloudMessagingRepository
import javax.inject.Inject

class UpdatePrivateMessagesSubscriptionUseCase @Inject constructor(
    private val cloudMessagingRepository: CloudMessagingRepository,
) {

    fun execute(value: Boolean) =
        if (value) {
            cloudMessagingRepository.subscribeToPrivateMessages()
        } else {
            cloudMessagingRepository.unsubscribeFromPrivateMessages()
        }
}
