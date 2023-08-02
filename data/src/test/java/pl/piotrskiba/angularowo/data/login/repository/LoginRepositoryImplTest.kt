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
import pl.piotrskiba.angularowo.domain.login.model.AccessTokenError
import pl.piotrskiba.angularowo.domain.login.model.AccessTokenModel
import java.io.IOException

class LoginRepositoryImplTest {

    val userCode = "user_code"
    val loginApi: LoginApiService = mockk()
    val tested = LoginRepositoryImpl(loginApi)

    @Test
    fun `SHOULD call registerDevice on loginApi WHEN registerDevice called`() {
        every { loginApi.registerDevice(userCode) } returns Single.never()

        tested.registerDevice(userCode)

        verify { loginApi.registerDevice(userCode) }
    }

    @Test
    fun `SHOULD return value WHEN loginApi registerDevice succeeded`() {
        mockkStatic(AccessTokenRemote::toDomain) {
            val accessTokenRemote: AccessTokenRemote = mockk()
            val accessTokenModel: AccessTokenModel = mockk()
            every { loginApi.registerDevice(userCode) } returns Single.just(
                accessTokenRemote,
            )
            every { accessTokenRemote.toDomain() } returns accessTokenModel

            val result = tested.registerDevice(userCode).test()

            result.assertValue(accessTokenModel)
        }
    }

    @Test
    fun `SHOULD throw error WHEN loginApi registerDevice failed`() {
        every { loginApi.registerDevice(userCode) } returns Single.error(IOException())

        val result = tested.registerDevice(userCode).test()

        result.assertError(AccessTokenError::class.java)
    }
}
