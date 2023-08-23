package pl.piotrskiba.angularowo.domain.settings.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import org.amshove.kluent.assertSoftly
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.cloudmessaging.repository.CloudMessagingRepository

class UpdateAccountIncidentsSubscriptionUseCaseTest {

    val cloudMessagingRepository: CloudMessagingRepository = mockk()
    val preferencesRepository: PreferencesRepository = mockk()
    val tested = UpdateAccountIncidentsSubscriptionUseCase(cloudMessagingRepository, preferencesRepository)

    @Test
    fun `SHOULD subscribe to account incidents topic AND save the value in preferences WHEN value is true`() {
        every { cloudMessagingRepository.subscribeToAccountIncidents() } returns Completable.complete()
        every { preferencesRepository.setSubscribedToFirebaseAccountIncidentsTopic(true) } returns Completable.complete()

        val result = tested.execute(true).test()

        assertSoftly {
            verify {
                cloudMessagingRepository.subscribeToAccountIncidents()
                preferencesRepository.setSubscribedToFirebaseAccountIncidentsTopic(true)
            }
            result.assertComplete()
        }
    }

    @Test
    fun `SHOULD unsubscribe from account incidents topic AND save the value in preferences WHEN value is false`() {
        every { cloudMessagingRepository.unsubscribeFromAccountIncidents() } returns Completable.complete()
        every { preferencesRepository.setSubscribedToFirebaseAccountIncidentsTopic(false) } returns Completable.complete()

        val result = tested.execute(false).test()

        assertSoftly {
            verify {
                cloudMessagingRepository.unsubscribeFromAccountIncidents()
                preferencesRepository.setSubscribedToFirebaseAccountIncidentsTopic(false)
            }
            result.assertComplete()
        }
    }
}
