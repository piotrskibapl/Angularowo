package pl.piotrskiba.angularowo.domain.login.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.amshove.kluent.assertSoftly
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.cloudmessaging.repository.CloudMessagingRepository
import pl.piotrskiba.angularowo.domain.login.model.AccessTokenModel
import pl.piotrskiba.angularowo.domain.login.repository.LoginRepository

class RegisterDeviceUseCaseTest {

    val loginRepository: LoginRepository = mockk()
    val preferencesRepository: PreferencesRepository = mockk(relaxed = true)
    val cloudMessagingRepository: CloudMessagingRepository = mockk(relaxed = true)
    val tested = RegisterDeviceUseCase(loginRepository, preferencesRepository, cloudMessagingRepository)

    @Test
    fun `SHOULD register device`() {
        val userCode = "userCode"
        val accessTokenModel: AccessTokenModel = mockk(relaxed = true)
        every { loginRepository.registerDevice(userCode) } returns Single.just(accessTokenModel)
        every { cloudMessagingRepository.subscribeToPlayerUuid(any()) } returns Completable.complete()
        every { preferencesRepository.setUuid(any()) } returns Completable.complete()
        every { preferencesRepository.setUsername(any()) } returns Completable.complete()
        every { preferencesRepository.setAccessToken(any()) } returns Completable.complete()

        val result = tested.execute(userCode).test()

        result.assertValue(accessTokenModel)
    }

    @Test
    fun `SHOULD subscribe to FCM topic WHEN device registered`() {
        val userCode = "userCode"
        val accessTokenModel: AccessTokenModel = mockk(relaxed = true) {
            every { uuid } returns "uuid"
        }
        every { loginRepository.registerDevice(userCode) } returns Single.just(accessTokenModel)

        val result = tested.execute(userCode).test()

        assertSoftly {
            verify { cloudMessagingRepository.subscribeToPlayerUuid("uuid") }
            result.assertNotComplete()
        }
    }

    @Test
    fun `SHOULD save user data in preferences WHEN device registered`() {
        val userCode = "userCode"
        val accessTokenModel: AccessTokenModel = mockk(relaxed = true) {
            every { uuid } returns "uuid"
            every { username } returns "username"
            every { accessToken } returns "accessToken"
        }
        every { loginRepository.registerDevice(userCode) } returns Single.just(accessTokenModel)

        val result = tested.execute(userCode).test()

        assertSoftly {
            verify { preferencesRepository.setUuid("uuid") }
            verify { preferencesRepository.setUsername("username") }
            verify { preferencesRepository.setAccessToken("accessToken") }
            result.assertNotComplete()
        }
    }
}
