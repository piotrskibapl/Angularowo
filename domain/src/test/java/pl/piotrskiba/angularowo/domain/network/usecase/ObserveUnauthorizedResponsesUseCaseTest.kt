package pl.piotrskiba.angularowo.domain.network.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.TestScheduler
import org.amshove.kluent.assertSoftly
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import pl.piotrskiba.angularowo.domain.network.repository.NetworkRepository
import pl.piotrskiba.angularowo.domain.settings.usecase.LogoutUserUseCase
import java.util.concurrent.TimeUnit

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ObserveUnauthorizedResponsesUseCaseTest {

    val testScheduler = TestScheduler()
    val networkRepository: NetworkRepository = mockk()
    val logoutUserUseCase: LogoutUserUseCase = mockk()
    val tested = ObserveUnauthorizedResponsesUseCase(networkRepository, logoutUserUseCase)

    @BeforeAll
    fun setup() {
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
    }

    @Test
    fun `SHOULD signal unauthorized response WHEN networkRepository signals unauthorized response`() {
        val appVersionCode = 123
        every { networkRepository.observeUnauthorizedResponses() } returns Observable.just(Unit).delay(1, TimeUnit.HOURS)
        every { logoutUserUseCase.execute(appVersionCode) } returns Completable.complete()

        val result = tested.execute(appVersionCode).test()

        assertSoftly {
            testScheduler.advanceTimeBy(59, TimeUnit.MINUTES)
            result.assertValueCount(0)
            testScheduler.advanceTimeBy(1, TimeUnit.MINUTES)
            result.assertValueCount(1)
        }
    }

    @Test
    fun `SHOULD logout WHEN unauthorized response occurred`() {
        val appVersionCode = 123
        every { networkRepository.observeUnauthorizedResponses() } returns Observable.just(Unit)
        every { logoutUserUseCase.execute(appVersionCode) } returns Completable.complete()

        val result = tested.execute(appVersionCode).test()

        verify {
            logoutUserUseCase.execute(appVersionCode)
            result.assertComplete()
        }
    }
}
