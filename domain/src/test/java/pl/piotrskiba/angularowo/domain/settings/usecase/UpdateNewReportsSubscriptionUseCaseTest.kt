package pl.piotrskiba.angularowo.domain.settings.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import org.amshove.kluent.assertSoftly
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.cloudmessaging.repository.CloudMessagingRepository

class UpdateNewReportsSubscriptionUseCaseTest {

    val cloudMessagingRepository: CloudMessagingRepository = mockk()
    val preferencesRepository: PreferencesRepository = mockk()
    val tested = UpdateNewReportsSubscriptionUseCase(cloudMessagingRepository, preferencesRepository)

    @Test
    fun `SHOULD subscribe to new reports topic AND save the value in preferences WHEN value is true`() {
        every { cloudMessagingRepository.subscribeToNewReports() } returns Completable.complete()
        every { preferencesRepository.setSubscribedToFirebaseNewReportsTopic(true) } returns Completable.complete()

        val result = tested.execute(true).test()

        assertSoftly {
            verify {
                cloudMessagingRepository.subscribeToNewReports()
                preferencesRepository.setSubscribedToFirebaseNewReportsTopic(true)
            }
            result.assertComplete()
        }
    }

    @Test
    fun `SHOULD unsubscribe from new reports topic AND save the value in preferences WHEN value is false`() {
        every { cloudMessagingRepository.unsubscribeFromNewReports() } returns Completable.complete()
        every { preferencesRepository.setSubscribedToFirebaseNewReportsTopic(false) } returns Completable.complete()

        val result = tested.execute(false).test()

        assertSoftly {
            verify {
                cloudMessagingRepository.unsubscribeFromNewReports()
                preferencesRepository.setSubscribedToFirebaseNewReportsTopic(false)
            }
            result.assertComplete()
        }
    }
}
