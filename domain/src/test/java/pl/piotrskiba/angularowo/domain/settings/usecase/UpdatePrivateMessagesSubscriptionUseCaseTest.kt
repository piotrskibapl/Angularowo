package pl.piotrskiba.angularowo.domain.settings.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import org.amshove.kluent.assertSoftly
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.cloudmessaging.repository.CloudMessagingRepository

class UpdatePrivateMessagesSubscriptionUseCaseTest {

    val cloudMessagingRepository: CloudMessagingRepository = mockk()
    val preferencesRepository: PreferencesRepository = mockk()
    val tested = UpdatePrivateMessagesSubscriptionUseCase(cloudMessagingRepository, preferencesRepository)

    @Test
    fun `SHOULD subscribe to private messages topic AND save the value in preferences WHEN value is true`() {
        every { cloudMessagingRepository.subscribeToPrivateMessages() } returns Completable.complete()
        every { preferencesRepository.setSubscribedToFirebasePrivateMessagesTopic(true) } returns Completable.complete()

        val result = tested.execute(true).test()

        assertSoftly {
            verify {
                cloudMessagingRepository.subscribeToPrivateMessages()
                preferencesRepository.setSubscribedToFirebasePrivateMessagesTopic(true)
            }
            result.assertComplete()
        }
    }

    @Test
    fun `SHOULD unsubscribe from private messages topic AND save the value in preferences WHEN value is false`() {
        every { cloudMessagingRepository.unsubscribeFromPrivateMessages() } returns Completable.complete()
        every { preferencesRepository.setSubscribedToFirebasePrivateMessagesTopic(false) } returns Completable.complete()

        val result = tested.execute(false).test()

        assertSoftly {
            verify {
                cloudMessagingRepository.unsubscribeFromPrivateMessages()
                preferencesRepository.setSubscribedToFirebasePrivateMessagesTopic(false)
            }
            result.assertComplete()
        }
    }
}
