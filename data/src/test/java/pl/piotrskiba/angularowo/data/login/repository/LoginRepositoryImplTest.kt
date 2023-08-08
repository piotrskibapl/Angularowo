package pl.piotrskiba.angularowo.data.login.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import pl.piotrskiba.angularowo.data.login.LoginApiService
import pl.piotrskiba.angularowo.data.login.model.AccessTokenRemote
import pl.piotrskiba.angularowo.data.login.model.toDomain
import pl.piotrskiba.angularowo.domain.login.model.AccessTokenError
import pl.piotrskiba.angularowo.domain.login.model.AccessTokenModel
import java.io.IOException

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoginRepositoryImplTest {

    val userCode = "user_code"
    val loginApi: LoginApiService = mockk()
    val tested = LoginRepositoryImpl(loginApi)

    @BeforeAll
    fun setup() {
        mockkStatic(AccessTokenRemote::toDomain)
    }

    @AfterAll
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `SHOULD return value WHEN registerDevice succeeded`() {
        val accessTokenModel: AccessTokenModel = mockk()
        val accessTokenRemote: AccessTokenRemote = mockk {
            every { toDomain() } returns accessTokenModel
        }
        every { loginApi.registerDevice(userCode) } returns Single.just(accessTokenRemote)

        val result = tested.registerDevice(userCode).test()

        result.assertValue(accessTokenModel)
    }

    @Test
    fun `SHOULD throw error WHEN registerDevice failed`() {
        every { loginApi.registerDevice(userCode) } returns Single.error(IOException())

        val result = tested.registerDevice(userCode).test()

        result.assertError(AccessTokenError::class.java)
    }
}
