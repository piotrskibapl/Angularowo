package pl.piotrskiba.angularowo.data.network.repository

import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.data.network.interceptors.UnauthorizedInterceptor

class NetworkRepositoryImplTest {

    val unauthorizedInterceptor: UnauthorizedInterceptor = mockk()
    val tested = NetworkRepositoryImpl(unauthorizedInterceptor)

    @Test
    fun `SHOULD observe unauthorized responses`() {
        val responsesObservable: Observable<Unit> = mockk()
        val responsesPublishSubject: PublishSubject<Unit> = mockk {
            every { hide() } returns responsesObservable
        }
        every { unauthorizedInterceptor.unauthorizedResponses } returns responsesPublishSubject

        tested.observeUnauthorizedResponses() shouldBeEqualTo responsesObservable
    }
}
