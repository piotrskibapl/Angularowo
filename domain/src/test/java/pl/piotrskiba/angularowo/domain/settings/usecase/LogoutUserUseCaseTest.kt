package pl.piotrskiba.angularowo.domain.settings.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import org.amshove.kluent.assertSoftly
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.cloudmessaging.repository.CloudMessagingRepository

class LogoutUserUseCaseTest {

    val preferencesRepository: PreferencesRepository = mockk()
    val cloudMessagingRepository: CloudMessagingRepository = mockk()
    val tested = LogoutUserUseCase(preferencesRepository, cloudMessagingRepository)

    @Test
    fun `SHOULD unsubscribe from cloud messaging topics AND clear user data WHEN execute called`() {
        val appVersionCode = 123
        val uuid = "uuid"
        val rankName = "rank"
        every { preferencesRepository.uuid() } returns Maybe.just(uuid)
        every { preferencesRepository.rankName() } returns Maybe.just(rankName)
        every { cloudMessagingRepository.unsubscribeFromAppVersion(appVersionCode) } returns Completable.complete()
        every { cloudMessagingRepository.unsubscribeFromPlayerUuid(uuid) } returns Completable.complete()
        every { cloudMessagingRepository.unsubscribeFromPlayerRank(rankName) } returns Completable.complete()
        every { cloudMessagingRepository.unsubscribeFromNewEvents() } returns Completable.complete()
        every { cloudMessagingRepository.unsubscribeFromPrivateMessages() } returns Completable.complete()
        every { cloudMessagingRepository.unsubscribeFromAccountIncidents() } returns Completable.complete()
        every { cloudMessagingRepository.unsubscribeFromNewReports() } returns Completable.complete()
        every { preferencesRepository.clearUserData() } returns Completable.complete()

        val result = tested.execute(appVersionCode).test()

        assertSoftly {
            verify {
                cloudMessagingRepository.unsubscribeFromAppVersion(appVersionCode)
                cloudMessagingRepository.unsubscribeFromPlayerUuid(uuid)
                cloudMessagingRepository.unsubscribeFromPlayerRank(rankName)
                cloudMessagingRepository.unsubscribeFromNewEvents()
                cloudMessagingRepository.unsubscribeFromPrivateMessages()
                cloudMessagingRepository.unsubscribeFromAccountIncidents()
                cloudMessagingRepository.unsubscribeFromNewReports()
                preferencesRepository.clearUserData()
            }
            result.assertComplete()
        }
    }

    @Test
    fun `SHOULD return error WHEN uuid is empty`() {
        every { preferencesRepository.uuid() } returns Maybe.empty()
        every { preferencesRepository.rankName() } returns Maybe.just("rank")

        val result = tested.execute(123).test()

        result.assertError(NoSuchElementException::class.java)
    }

    @Test
    fun `SHOULD return error WHEN rankName is empty`() {
        every { preferencesRepository.uuid() } returns Maybe.just("uuid")
        every { preferencesRepository.rankName() } returns Maybe.empty()

        val result = tested.execute(123).test()

        result.assertError(NoSuchElementException::class.java)
    }
}
