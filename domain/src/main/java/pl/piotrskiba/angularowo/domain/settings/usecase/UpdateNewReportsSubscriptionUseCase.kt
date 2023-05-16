package pl.piotrskiba.angularowo.domain.settings.usecase

import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.cloudmessaging.repository.CloudMessagingRepository
import javax.inject.Inject

class UpdateNewReportsSubscriptionUseCase @Inject constructor(
    private val cloudMessagingRepository: CloudMessagingRepository,
    private val preferencesRepository: PreferencesRepository,
) {

    fun execute(value: Boolean) =
        if (value) {
            cloudMessagingRepository.subscribeToNewReports()
        } else {
            cloudMessagingRepository.unsubscribeFromNewReports()
        }.andThen(preferencesRepository.setSubscribedToFirebaseNewReportsTopic(value))
}
