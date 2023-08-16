package pl.piotrskiba.angularowo.domain.login.usecase

import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Maybe
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository

class CheckIfUserIsLoggedInUseCaseTest {

    val preferencesRepository: PreferencesRepository = mockk()
    val tested = CheckIfUserIsLoggedInUseCase(preferencesRepository)

    @Test
    fun `SHOULD return true WHEN accessToken and uuid are set`() {
        every { preferencesRepository.accessToken() } returns Maybe.just("accessToken")
        every { preferencesRepository.uuid() } returns Maybe.just("uuid")

        val result = tested.execute().test()

        result.assertValue(true)
    }

    @Test
    fun `SHOULD return false WHEN accessToken is not set`() {
        every { preferencesRepository.accessToken() } returns Maybe.empty()
        every { preferencesRepository.uuid() } returns Maybe.just("uuid")

        val result = tested.execute().test()

        result.assertValue(false)
    }

    @Test
    fun `SHOULD return false WHEN uuid is not set`() {
        every { preferencesRepository.accessToken() } returns Maybe.just("accessToken")
        every { preferencesRepository.uuid() } returns Maybe.empty()

        val result = tested.execute().test()

        result.assertValue(false)
    }
}
