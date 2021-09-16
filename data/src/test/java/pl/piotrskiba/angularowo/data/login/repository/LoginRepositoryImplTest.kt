package pl.piotrskiba.angularowo.data.login.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import io.reactivex.rxjava3.core.Single
import org.junit.Test
import pl.piotrskiba.angularowo.data.login.LoginApiService
import pl.piotrskiba.angularowo.data.login.model.AccessTokenData
import pl.piotrskiba.angularowo.data.login.model.toDomain
import pl.piotrskiba.angularowo.domain.login.model.AccessToken
import pl.piotrskiba.angularowo.domain.login.model.AccessTokenError
import java.io.IOException

class LoginRepositoryImplTest {

    val API_KEY = "api_key"
    val USER_CODE = "user_code"
    val loginApi: LoginApiService = mockk()
    val tested = LoginRepositoryImpl(loginApi)

    @Test
    fun `SHOULD call registerDevice on loginApi WHEN registerDevice called`() {
        every { loginApi.registerDevice(API_KEY, USER_CODE) } returns Single.never()

        tested.registerDevice(API_KEY, USER_CODE)

        verify { loginApi.registerDevice(API_KEY, USER_CODE) }
    }

    @Test
    fun `SHOULD return value WHEN loginApi registerDevice succeeded`() {
        mockkStatic(AccessTokenData::toDomain) {
            val accessTokenData: AccessTokenData = mockk()
            val accessToken: AccessToken = mockk()
            every { loginApi.registerDevice(API_KEY, USER_CODE) } returns Single.just(
                accessTokenData
            )
            every { accessTokenData.toDomain() } returns accessToken

            val result = tested.registerDevice(API_KEY, USER_CODE).test()

            result.assertValue(accessToken)
        }
    }

    @Test
    fun `SHOULD throw error WHEN loginApi registerDevice failed`() {
        every { loginApi.registerDevice(API_KEY, USER_CODE) } returns Single.error(IOException())

        val result = tested.registerDevice(API_KEY, USER_CODE).test()

        result.assertError(AccessTokenError::class.java)
    }
}
