package pl.piotrskiba.angularowo.domain.login.model

import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import retrofit2.HttpException
import java.io.IOException

@RunWith(Parameterized::class)
class AccessTokenErrorTest(
    val throwable: Throwable,
    val expectedAccessTokenError: AccessTokenError,
) {

    companion object {

        @JvmStatic
        @Parameterized.Parameters
        fun parameters() = listOf(
            arrayOf(
                mockk<HttpException> { every { code() } returns 403 },
                AccessTokenError.CodeExpiredError
            ),
            arrayOf(
                mockk<HttpException> { every { code() } returns 404 },
                AccessTokenError.CodeNotFoundError
            ),
            arrayOf(
                mockk<HttpException> { every { code() } returns 500 },
                AccessTokenError.UnknownError
            ),
            arrayOf(
                IOException(),
                AccessTokenError.UnknownError
            ),
        )
    }

    @Test
    fun `SHOULD map Throwable to AccessTokenError WHEN toAccessTokenError called`() {
        val result = throwable.toAccessTokenError()

        assert(result == expectedAccessTokenError)
    }
}
