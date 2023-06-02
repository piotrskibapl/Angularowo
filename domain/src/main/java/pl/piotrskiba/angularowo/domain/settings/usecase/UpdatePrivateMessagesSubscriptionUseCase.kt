package pl.piotrskiba.angularowo.domain.settings.usecase

import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.cloudmessaging.repository.CloudMessagingRepository
import javax.inject.Inject

class UpdatePrivateMessagesSubscriptionUseCase @Inject constructor(
    private val cloudMessagingRepository: CloudMessagingRepository,
    private val preferencesRepository: PreferencesRepository,
) {

    fun execute(value: Boolean) =
        if (value) {
            cloudMessagingRepository.subscribeToPrivateMessages()
        } else {
            cloudMessagingRepository.unsubscribeFromPrivateMessages()
        }.andThen(preferencesRepository.setSubscribedToFirebasePrivateMessagesTopic(value))
}
