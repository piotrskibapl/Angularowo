package pl.piotrskiba.angularowo.domain.settings.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import org.amshove.kluent.assertSoftly
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.cloudmessaging.repository.CloudMessagingRepository

class UpdateNewEventsSubscriptionUseCaseTest {

    val cloudMessagingRepository: CloudMessagingRepository = mockk()
    val preferencesRepository: PreferencesRepository = mockk()
    val tested = UpdateNewEventsSubscriptionUseCase(cloudMessagingRepository, preferencesRepository)

    @Test
    fun `SHOULD subscribe to new events topic AND save the value in preferences WHEN value is true`() {
        every { cloudMessagingRepository.subscribeToNewEvents() } returns Completable.complete()
        every { preferencesRepository.setSubscribedToFirebaseEventsTopic(true) } returns Completable.complete()

        val result = tested.execute(true).test()

        assertSoftly {
            verify {
                cloudMessagingRepository.subscribeToNewEvents()
                preferencesRepository.setSubscribedToFirebaseEventsTopic(true)
            }
            result.assertComplete()
        }
    }

    @Test
    fun `SHOULD unsubscribe from new events topic AND save the value in preferences WHEN value is false`() {
        every { cloudMessagingRepository.unsubscribeFromNewEvents() } returns Completable.complete()
        every { preferencesRepository.setSubscribedToFirebaseEventsTopic(false) } returns Completable.complete()

        val result = tested.execute(false).test()

        assertSoftly {
            verify {
                cloudMessagingRepository.unsubscribeFromNewEvents()
                preferencesRepository.setSubscribedToFirebaseEventsTopic(false)
            }
            result.assertComplete()
        }
    }
}
