package pl.piotrskiba.angularowo.data.login.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.Test
import pl.piotrskiba.angularowo.data.login.LoginApiService
import pl.piotrskiba.angularowo.data.login.model.AccessTokenRemote
import pl.piotrskiba.angularowo.data.login.model.toDomain
import pl.piotrskiba.angularowo.domain.login.model.AccessToken
import pl.piotrskiba.angularowo.domain.login.model.AccessTokenError
import java.io.IOException

class LoginRepositoryImplTest {

    val USER_CODE = "user_code"
    val loginApi: LoginApiService = mockk()
    val tested = LoginRepositoryImpl(loginApi)

    @Test
    fun `SHOULD call registerDevice on loginApi WHEN registerDevice called`() {
        every { loginApi.registerDevice(any(), USER_CODE) } returns Single.never()

        tested.registerDevice(USER_CODE)

        verify { loginApi.registerDevice(any(), USER_CODE) }
    }

    @Test
    fun `SHOULD return value WHEN loginApi registerDevice succeeded`() {
        mockkStatic(AccessTokenRemote::toDomain) {
            val accessTokenRemote: AccessTokenRemote = mockk()
            val accessToken: AccessToken = mockk()
            every { loginApi.registerDevice(any(), USER_CODE) } returns Single.just(
                accessTokenRemote
            )
            every { accessTokenRemote.toDomain() } returns accessToken

            val result = tested.registerDevice(USER_CODE).test()

            result.assertValue(accessToken)
        }
    }

    @Test
    fun `SHOULD throw error WHEN loginApi registerDevice failed`() {
        every { loginApi.registerDevice(any(), USER_CODE) } returns Single.error(IOException())

        val result = tested.registerDevice(USER_CODE).test()

        result.assertError(AccessTokenError::class.java)
    }
}
